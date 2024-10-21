package com.onecab.features.screens.complete_orders_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.repository.DebtRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.features.common_component.mergingListGoods
import com.onecab.features.di.IoDispatcher
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class CompleteOrderInfoUiState(
    val orderEntity: OrderEntity = OrderEntity(),
    val consist: String = "",
    val weight: String = "",
    val debt: String = "",
)

@HiltViewModel
class CompleteOrderInfoViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
    private val debtRepository: DebtRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(CompleteOrderInfoUiState())
    val state = _state.asStateFlow()

    fun fetchScreen(orderId: String) {
        val orderEntity = orderRepository.getCompleteOrderByIdFromCash(orderId = orderId)

        // объединим два списка товаров через специальную фуекцию
        val updateList = mergingListGoods(
            originalGoodsList = orderEntity.goodsFromServer,
            modifyGoodsList = orderEntity.goodsModified
        )

        val commonWeight = updateList
            .filter { it.unitName == "кг" }
            .sumOf { goodEntity ->
                if (goodEntity.goodHasBeenModified)
                    goodEntity.modifyQuantity
                else
                    goodEntity.quantity
            }

        _state.value = state.value.copy(
            orderEntity = orderEntity,
            consist = updateList.size.toString(),
            weight = commonWeight.toMyFormat()
            // задолженность догружаем ниже отдельно
        )

        // Догрузим сумму задолженности
        loadDebtListFromServer(orderId = orderId)
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

    private fun Double.toMyFormat(): String {
        return String.format(Locale.UK, "%.2f", this)
    }

    fun onClickArrowBack() {
        navigationService.onClickArrowBack()
    }

    fun navToOrdersContent(orderId: String) {
        navigationService.navigateWithStringArgument(
            route = Screens.CompleteOrdersContentScreen.route,
            nameValue = "orderId",
            argumentValue = orderId
        )
    }
}