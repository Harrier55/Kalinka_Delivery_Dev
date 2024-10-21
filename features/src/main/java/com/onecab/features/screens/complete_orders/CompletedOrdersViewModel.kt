package com.onecab.features.screens.complete_orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_1
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_2
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_3
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_lond_to_string_4
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_lond_to_string_5
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.repository.OrderRepository
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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompletedOrdersScreenUiState(

    val currentDate: String = "11.11.1111",
    val dateFoRequest: String = "",
    val today: String = "",
    val ordersList: List<OrderEntity> = emptyList(),
    val ordersListOriginal: List<OrderEntity> = emptyList(),
    val isLoading: Boolean = false,
    val showSelectDateDialog: Boolean = false,
)


@HiltViewModel
class CompletedOrdersViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
    private val registerRepository: RegisterRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(CompletedOrdersScreenUiState())
    val state = _state.asStateFlow()

    init {
        // Получить первоначальную дату для запроса
        _state.value = state.value.copy(
            today = DateFormatterMyApplication.getCurrentDateForRequest(),
            dateFoRequest = DateFormatterMyApplication.getCurrentDateForRequest(),
            currentDate = DateFormatterMyApplication.getCurrentDateForCalendarButton()
        )
    }

    // Получить список заказов с сервера по указанной дате (по умолчанию - текущей)
    // обработать и закэшировать
    fun fillScreen(dateRequest: String) {

        // если дата пришла не пустая, изменим ее на требуемую
        if (dateRequest.isNotEmpty() && dateRequest != "empty") {
            _state.value = state.value.copy(
                dateFoRequest = dateRequest.date_formatter_3(),
                currentDate = dateRequest.date_formatter_1()
            )
        }

        viewModelScope.launch(ioDispatcher) {
            _state.value = state.value.copy(isLoading = true)
            delay(500)

            /* Логика следующая
            Проверить, есть ли в списке приходящих с сервера заказов такие, в которых
            были изменения товаров, если таковые есть - пометить их иконкой
            */

            // 1. Получить завершенные заказы с сервера (по дате )
            val serverListOrders = orderRepository
                .getOrdersFromServer(
                    date = state.value.dateFoRequest,
                    token = registerRepository.getAuthToken().token ?: ""
                )
                // оставляем только завершенные заказы
                // но пришла новая установка - отображаем на сегодня с фильтром
                // а на любой другой день - без фильтра, т.е все заказы
                .filter { orderEntity ->
                    if (state.value.today == state.value.dateFoRequest) {
                        orderEntity.status.isNotEmpty()
                    } else {
                        true
                    }
                }

            //  2. Получим список модифицированных заказов (товаров) из БД
            val listModifyOrders = orderRepository.getModifyOrderFromDb()

            // объединим эти два списка с помощью специальной функции
            val combinedOrdersList = mergingListOrders(
                serverOrders = serverListOrders,
                modifyOrders = listModifyOrders
            )

            // Визуальные преобразования
            val resultList = combinedOrdersList
                // Отобразить дату в нужном формате
                .map { it.copy(deliveryDate = it.deliveryDate.date_formatter_2()) }
                // сортировать по дате
                .sortedBy { orderEntity -> orderEntity.deliveryDate }

            // Сохранить полученный список в кэш репозитория
            orderRepository.setCompleteOrdersToCash(completeOrders = resultList)

            _state.value = state.value.copy(
                isLoading = false,
                ordersList = resultList,
                ordersListOriginal = resultList
            )
        }
    }

    // Выбот даты при нажатии кнопки выбора даты в диалоге
    fun onClickSelectedDate(selectDate: Long?) {
        closeSelectDateDialog()
        selectDate?.let {
            _state.value = state.value.copy(
                currentDate = it.date_formatter_lond_to_string_4(),
                dateFoRequest = it.date_formatter_lond_to_string_5()
            )
        }
        fillScreen("")
    }

    fun showSelectDateDialog() {
        _state.value = state.value.copy(
            showSelectDateDialog = true
        )
    }

    fun closeSelectDateDialog() {
        _state.value = state.value.copy(
            showSelectDateDialog = false
        )
    }

    fun onClickOrderItem(orderId: String) {
        navigationService.navigateWithStringArgument(
            route = Screens.CompleteOrdersInfoScreen.route,
            nameValue = "orderId",
            argumentValue = orderId
        )
    }

    // Функция поиска
    fun searchFunction(text: String) {
        _state.value = state.value.copy(
            ordersList = state.value.ordersListOriginal.filter {
                it.purchaserName.lowercase().contains(text.lowercase()) ||
                        it.deliveryAddress.lowercase().contains(text.lowercase())
            }
        )
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }
}