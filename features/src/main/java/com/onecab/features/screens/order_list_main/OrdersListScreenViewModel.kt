package com.onecab.features.screens.order_list_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_2
import com.onecab.domain.crashlytics.CrashlyticsManager
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.repository.ParamsRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.common_component.mergingListOrders
import com.onecab.features.di.IoDispatcher
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersListScreenUiState(
    val listOrders: List<OrderEntity> = emptyList(),
    val listOrdersOriginal: List<OrderEntity> = emptyList(),
    val isLoading: Boolean = false,
    val showUpdateBlock: Boolean = false,
)


@HiltViewModel
class OrdersListScreenViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val navigationService: NavigationService,
    private val orderRepository: OrderRepository,
    private val registerRepository: RegisterRepository,
    private val paramsRepository: ParamsRepository,
    private val crashlyticsManager: CrashlyticsManager,
    private val localSourceRepository: LocalSourceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersListScreenUiState())
    val state = _state.asStateFlow()

    init {
        // регистрируем текущего пользователя в системе аналитики
        val currentUser = localSourceRepository.getLogin()
        crashlyticsManager.setUserIdentifier(currentUser)
    }

    fun fillFirstScreen() {
        // если загрузка самая первая - старт приложения - грузим сразу
        // если не первая - только по свайпу
        val counterLoading = orderRepository.isFirstInstance()
        if (counterLoading) {
            loadFromServer()
        } else {
            loadFromCash()
        }
    }

    fun fillBySwipeScreen() {
        loadFromServer()
    }

    private fun loadFromCash() {
        val inputListOrders = orderRepository
            .getOrdersFromCash()
            .filter { orderEntity -> orderEntity.status.isEmpty() } // фильтруем только недоставленные заказы

        _state.update { state ->
            state.copy(
                listOrders = inputListOrders,
                showUpdateBlock = inputListOrders.isEmpty()
            )
        }
    }

    fun loadFromServer() {

        orderRepository.setCountInstance()  // увеличить счетчик загрузок

        val currentDate = DateFormatterMyApplication.getCurrentDateForRequest() // Дата !!!

        Log.i(TAG, "--- Запрос заказов на $currentDate")

        viewModelScope.launch(ioDispatcher) {
            _state.value = state.value.copy(isLoading = true, showUpdateBlock = false)
            delay(500)

            // получим данные с сервера
            val serverListOrders = orderRepository.getOrdersFromServer(
                date = currentDate,
                token = registerRepository.getAuthToken().token ?: ""
            )
            // получим данные о модифицированных товарах из БД
            val localListOrders = orderRepository.getModifyOrderFromDb()
            // объединим эти два списка с помощью специальной функции
            val combinedOrdersList = mergingListOrders(
                serverOrders = serverListOrders,
                modifyOrders = localListOrders
            )
            // Визуальные преобразования
            val resultList = combinedOrdersList
                .filter { orderEntity -> orderEntity.status.isEmpty() } // только недоставленные
                .map { it.copy(deliveryDate = it.deliveryDate.date_formatter_2()) } // формат даты

            // Обновим кэш списка заказов
            orderRepository.updateCashOrderList(ordersList = resultList)

            _state.update { state ->
                state.copy(
                    listOrders = resultList,
                    listOrdersOriginal = resultList,
                    isLoading = false,
                    showUpdateBlock = resultList.isEmpty()
                )
            }

            // ждем две секунды и подгружаем данные для параметров оплаты
            delay(2_000)
            loadParamsFromServer(token = registerRepository.getAuthToken().token ?: "")
        }
    }

    // Функция поиска
    fun searchFunction(text: String) {
        _state.value = state.value.copy(
            listOrders = state.value.listOrdersOriginal.filter {
                it.purchaserName.lowercase().contains(text.lowercase()) ||
                        it.deliveryAddress.lowercase().contains(text.lowercase())
            }
        )
    }

    fun onClickOrderItem(orderId: String) {
        if (orderId.isNotEmpty()) {
            navigationService.navigateToOrderInfoScreen(
                route = Screens.OrdersInfoScreen.route,
                orderId = orderId,
                invoiceBarcode = "empty"
            )
        }
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }

    fun navToScan() {
        navigationService.navigateWithoutArgument(
            route = Screens.ScannerScreen.route
        )
    }

    // Подгрузим с сервера данные для проведения оплаты
    private suspend fun loadParamsFromServer(token: String) {
        val response = paramsRepository.getParamsFromServer(token)
        response.fold(
            onSuccess = { value ->
                if (value.isNotEmpty()) paramsRepository.saveParamsToCash(params = value)
            },
            onFailure = {
                //todo
            }
        )
    }
}