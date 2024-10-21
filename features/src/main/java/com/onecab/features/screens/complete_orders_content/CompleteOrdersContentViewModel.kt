package com.onecab.features.screens.complete_orders_content

import androidx.lifecycle.ViewModel
import com.onecab.domain.entity.GoodsEntity
import com.onecab.domain.repository.OrderRepository
import com.onecab.features.common_component.mergingListGoods
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject


data class CompleteOrderContentUiState(
    val orderId: String = "",
    val quantity: String = "",
    val unit: String = "",
    val count: String = "",
    val countName: String = "",
    var listGoods: List<GoodsEntity> = emptyList(),
)

@HiltViewModel
class CompleteOrdersContentViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _stateForContent = MutableStateFlow(CompleteOrderContentUiState())
    val stateForContent = _stateForContent.asStateFlow()

    // Также, логика заполнения списка заключается в получении списка товаров с сервера и списка
    // модифицированных товаров и их объединеие
    fun fetchContentScreen(orderId: String){

        val orderEntity = orderRepository.getCompleteOrderByIdFromCash(orderId = orderId)

        // объединим два списка товаров через специальную фуекцию
        val updateList = mergingListGoods(
            originalGoodsList = orderEntity.goodsFromServer,
            modifyGoodsList = orderEntity.goodsModified
        )


//        val goodsList = orderRepository.getGoodsByCompleteOrderId(
//            orderId = orderId
//        )
//        val count = goodsList.size.toString()
        val countPosition = updateList.size.toString()
//        val commonWeight = goodsList.filter { it.unitName == "кг" }.sumOf { it.quantity }

        val commonWeight = updateList
            .filter { it.unitName == "кг" }
            .sumOf { goodEntity ->
                if (goodEntity.goodHasBeenModified)
                    goodEntity.modifyQuantity
                else
                    goodEntity.quantity
            }

        _stateForContent.value = stateForContent.value.copy(
            quantity = commonWeight.toMyFormat(),
            unit = "кг",
            count = countPosition,
            countName = countPosition.syntaxPosition(),
            listGoods = updateList
        )
    }

    private fun Double.toMyFormat(): String {
        return String.format(Locale.UK, "%.3f", this)
    }

    // в зависимости от количества вернет правильное слово с точки зрения синтаксиса
    private fun String.syntaxPosition(): String {
        val mySet = setOf("11", "12", "13", "14")
        val mySet2 = setOf("2", "3", "4")

        return when {
            this.length >= 2 && this.substring(this.length - 2) in mySet -> "позиций"
            this.substring(this.length - 1) in mySet2 -> "позиции"
            this.substring(this.length - 1) == "1" -> "позиция"
            else -> "позиций"
        }
    }

    fun onClickArrowBack() {
        navigationService.onClickArrowBack()
    }
}