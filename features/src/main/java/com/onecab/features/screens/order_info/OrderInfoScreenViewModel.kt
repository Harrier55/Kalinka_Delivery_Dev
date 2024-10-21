package com.onecab.features.screens.order_info

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.core.utils.DateFormatterMyApplication.date_formatter_6
import com.onecab.domain.entity.AddOrderStatusEntity
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.enums.Destination
import com.onecab.domain.repository.DebtRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.domain.response_entity.KalinkaResultResponse
import com.onecab.features.common_component.mergingListGoods
import com.onecab.features.di.IoDispatcher
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

data class OrderInfoUiState(
    val orderEntity: OrderEntity = OrderEntity(),
    val consist: String = "",
    val weight: String = "",
    val debt: String = "",
    val amountCalculated: String = "",
    val openDialog: Boolean = false,
    val isLoading: Boolean = false,
    val tickYes: Boolean = false,
    val tickNo: Boolean = false,
    val showSelectMapsSheet: Boolean = false,
    val showEmptyOrderDialog: Boolean = false,
)

private const val TAG = "OrderInfoScreenViewModel"

@HiltViewModel
class OrderInfoScreenViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
    private val debtRepository: DebtRepository,
    private val registerRepository: RegisterRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(OrderInfoUiState())
    val state = _state.asStateFlow()

    fun fillScreen(
        orderId: String,
        invoiceBarcode: String
    ) {
        // может прийти orderId,а может и штрих код, выбираем логику загрузки в зависимости от этого
        val orderEntity = if (orderId.isNotEmpty() && orderId != "empty") {
            orderRepository.getOrderByIdFromCash(orderId = orderId)
        } else if (invoiceBarcode.isNotEmpty() && invoiceBarcode != "empty") {
            orderRepository.getOrderByBarCode(invoiceBarcode = invoiceBarcode)
        } else {
            OrderEntity()
        }

        // Может возникнуть ситуация, что по штрихкоду заказ не найден
        if(orderEntity.orderId.isNotEmpty()){
            // Заказ найден, можно работать дальше

            // объединим два списка товаров через специальную фуекцию
            val updateList = mergingListGoods(
                originalGoodsList = orderEntity.goodsFromServer,
                modifyGoodsList = orderEntity.goodsModified
            )

            val consist = updateList.size

            val commonWeight = updateList
                .filter { it.unitName == "кг" }
                .sumOf { goodEntity ->
                    if (goodEntity.goodHasBeenModified)
                        goodEntity.modifyQuantity
                    else
                        goodEntity.quantity
                }

            val calculateAmount = updateList.sumOf { goodEntity ->
                if (goodEntity.goodHasBeenModified)
                    goodEntity.modifyQuantity * goodEntity.price
                else
                    goodEntity.quantity * goodEntity.price
            }

            _state.value = state.value.copy(
                orderEntity = orderEntity,
                consist = consist.toString(),
                weight = commonWeight.toMyFormat(),
                amountCalculated = calculateAmount.toMyFormat()
            )

            // Догрузим сумму задолженности
            loadDebtListFromServer(orderId = orderEntity.orderId)

        }else{
            // Заказ не найден - покажем диалог
            _state.value = state.value.copy(showEmptyOrderDialog = true)
        }
    }

    // Отметить, как доставленный
    fun onClickDelivery() {
        val token = registerRepository.getAuthToken().token ?: ""
        val newOrder = AddOrderStatusEntity(
            order_id = state.value.orderEntity.orderId,
            delivery_date = DateFormatterMyApplication.getCurrentLocalDateTime().date_formatter_6(),
            delivery_note = state.value.orderEntity.comment,
            status = Destination.delivered.name
        )

        viewModelScope.launch(ioDispatcher) {
            _state.value = state.value.copy(
                openDialog = true,
                isLoading = true
            )
            delay(300)

            // отправить запрос на сервер, что заказ доставлен
            val response = orderRepository.addOrderStatus(
                token = token,
                addOrderStatusEntity = newOrder
            )
            Log.d(TAG, "-- loadDeliveryOrderToSever ответ сервера === $response")

            when (response) {
                is KalinkaResultResponse.Success -> {
                    val result = response.data

                    if (result) {
                        // Ответ сервера положительный
                        _state.value = state.value.copy(
                            isLoading = false,
                            tickYes = true,
                            tickNo = false
                        )
                        delay(1_500)

                        withContext(Dispatchers.Main) {
                            // возврат на главную
                            // чтобы автоматически обновить данные с сервера - сбросим счетчик загрузок
                            orderRepository.resetCountInstance()
                            // и вернемся на главную страницу
                            navigationService.navigateWithoutArgument(
                                route = Screens.OrdersListScreen.route
                            )
                        }

                    } else {
                        _state.value = state.value.copy(
                            isLoading = false,
                            tickYes = false,
                            tickNo = true
                        )
                        delay(1_500)

                        // просто закрыть диалог
                        _state.value = state.value.copy(
                            tickYes = false,
                            tickNo = false,
                            openDialog = false
                        )
                    }
                }

                is KalinkaResultResponse.Error -> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        tickNo = true
                    )
                    delay(1_000)
                    _state.value = state.value.copy(
                        tickYes = false,
                        tickNo = false,
                        openDialog = false
                    )
                }
            }
        }
    }

    // при входе на экран запросить долги по контрагенту
    private fun loadDebtListFromServer(orderId: String) {
        viewModelScope.launch(ioDispatcher) {
            val resultRequest = debtRepository.getDebtListFromServer(
                orderId = orderId
            )

            resultRequest.fold(
                onSuccess = { list ->
                    val debt = list.sumOf { entity -> entity.amount }
                    _state.value = state.value.copy(
                        debt = debt.toMyFormat()
                    )
                },
                onFailure = {
                    _state.value = state.value.copy(
                        debt = "0"
                    )
                }
            )
        }
    }
    /*------------ navigate -------------------*/

    fun clickArrowBack() {
        navigationService.onClickArrowBack()
    }

    fun navToOrdersContent() {
        navigationService.navigateWithStringArgument(
            route = Screens.OrdersContentScreen.route,
            nameValue = "orderId",
            argumentValue = state.value.orderEntity.orderId
        )
    }

    fun navToDebtScreen() {
        navigationService.navigateWithStringArgument(
            route = Screens.OrdersDebtScreen.route,
            nameValue = "orderId",
            argumentValue = state.value.orderEntity.orderId
        )
    }

    fun navToPaymentScreen() {
        navigationService.navigateWithStringArgument(
            route = Screens.OrderForPaymentScreen.route,
            nameValue = "orderId",
            argumentValue = state.value.orderEntity.orderId
        )
    }

    fun closeEmptyOrderDialog() {
        navigationService.onClickArrowBack()
        _state.value = state.value.copy(showEmptyOrderDialog = false)
    }

    // работа с картами и геопозицией
    fun openSelectMapsSheet() {
        _state.value = state.value.copy(showSelectMapsSheet = true)
    }

    fun closeSelectMapsSheet() {
        _state.value = state.value.copy(showSelectMapsSheet = false)
    }

    fun showSelectedMaps(context: Context, item: Int, address: String) {

        val packageManager = context.packageManager

        when (item) {
            0 -> {
                // В браузер GoogleMaps
                try {
                    val searchUrl = "https://www.google.com/maps/search/?api=1&query=$address"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.d(TAG, "openSelectedMaps: Error")
                }
            }

            1 -> {
                // Для всех карт по geo схеме
                try {
                    packageManager.getPackageInfo("com.google.android.apps.maps", 0)
                    val googleMapsIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=$address")
                    )
                    if (googleMapsIntent.resolveActivity(packageManager) != null) {
                        context.startActivity(googleMapsIntent)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "openSelectedMaps: Error")
                }
            }

            2 -> {
                // В другом браузере, например в Яндекс
                try {
                    val searchUrl = "https://yandex.ru/maps/?text=$address"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.d(TAG, "openSelectedMaps: Error")
                }
            }

            else -> {}
        }
    }

    /*------------ Extentions func -------------------*/

    private fun Double.toMyFormat(): String {
        return String.format(Locale.UK, "%.2f", this)
    }
}