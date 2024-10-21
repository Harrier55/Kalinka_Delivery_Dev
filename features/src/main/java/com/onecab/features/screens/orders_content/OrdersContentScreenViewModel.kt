package com.onecab.features.screens.orders_content

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.core.utils.DateFormatterMyApplication
import com.onecab.domain.entity.GoodsEntity
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.repository.OrderRepository
import com.onecab.features.common_component.mergingListGoods
import com.onecab.features.di.IoDispatcher
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


data class OrdersContentScreenUiState(
    val orderId: String = "",
    val quantity: String = "",
    val unit: String = "",
    val count: String = "",
    val countName: String = "",
    val amount: String = "",
    val orderEntity: OrderEntity = OrderEntity(),
    val listGoods: List<GoodsEntity> = emptyList(),
    val goodsEntityForModified: GoodsEntity = GoodsEntity(),
    val showModifiedSheet: Boolean = false,
)

private const val TAG = "OrdersContentScreenViewModel"

@HiltViewModel
class OrdersContentScreenViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val navigationService: NavigationService,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersContentScreenUiState())
    val state = _state.asStateFlow()

    fun fetchScreen(orderId: String) {

        val orderEntity = orderRepository.getOrderByIdFromCash(orderId = orderId)

        // объединим два списка товаров через специальную фуекцию
        val updateList = mergingListGoods(
            originalGoodsList = orderEntity.goodsFromServer,
            modifyGoodsList = orderEntity.goodsModified
        )

        // Расчет суммирующих параметров
        val countPosition = updateList.size.toString()

        val commonWeight = updateList
            .filter { it.unitName == "кг" }
            .sumOf { goodEntity ->
                if (goodEntity.goodHasBeenModified)
                    goodEntity.modifyQuantity
                else
                    goodEntity.quantity
            }

        val summa = updateList.sumOf { goodEntity ->
            if (goodEntity.goodHasBeenModified)
                goodEntity.modifyQuantity * goodEntity.price
            else
                goodEntity.quantity * goodEntity.price
        }

        _state.value = state.value.copy(
            orderId = orderId,
            listGoods = updateList,
            quantity = commonWeight.toMyFormat(),
            count = countPosition,
            countName = countPosition.syntaxPosition(),
            unit = "кг",
            amount = summa.toMyFormat_2(),
            orderEntity = orderEntity
        )
    }

    // Открываем шторку для модификации количество товара, отределяем товар, который нужно изменить
    fun openModifiedSheet(commodityId: String) {
        val good = state.value.listGoods.find { it.commodityId == commodityId }
        good?.let {
            _state.value = state.value.copy(
                showModifiedSheet = true,
                goodsEntityForModified = it
            )
        }
    }

    // выполняем модификацию товара (один товар)
    fun goodsModify(
        changedQuantity: Double,
        goodsEntityForModified: GoodsEntity
    ) {

        val commodityId = goodsEntityForModified.commodityId

        // выполним проверку - все действия будем производить, если пользователь
        // действительно изменил количество товара
        val originalQuantity =
            state.value.listGoods.find { it.commodityId == commodityId }?.quantity ?: 0.0

        if (changedQuantity != originalQuantity) {
            // получим список только измененных товаров
            val modifyGoodsList = state.value.listGoods
                .map { goodsEntity ->
                    if (goodsEntity.commodityId == commodityId) {
                        goodsEntity.copy(
                            goodHasBeenModified = true,
                            modifyDate = DateFormatterMyApplication.getCurrentDateForRequest(),
                            modifyQuantity = changedQuantity,

                            )
                    } else {
                        goodsEntity
                    }
                }
                .filter { goodsEntity ->
                    goodsEntity.goodHasBeenModified
                }

            Log.d(TAG, " --список измененных товаров $modifyGoodsList")

            // обновляем закэшированый список в репозитории
            orderRepository.updateCashOrderList(
                orderId = state.value.orderId,
                modifyGoodsList = modifyGoodsList,
            )

            // Сохраняем изменения в БД
            viewModelScope.launch(ioDispatcher) {
                val saveResult = orderRepository.saveModifyOrderToDb(
                    orderId = state.value.orderId,
                    commodityId = goodsEntityForModified.commodityId,
                    quantity = changedQuantity,
                    modifyDate = DateFormatterMyApplication.getCurrentLocalDateTime()
                )
                Log.d(TAG, "Результат сохранения товара в БД = $saveResult")

                closeModifiedSheet()
                fetchScreen(orderId = state.value.orderId)
            }
        } else {
            // если вдруг пользователь позже снова вернулся к изначальному значению
            // то нужно удалить товар из этого списка и удалить запись из базы данных
            // как если бы (changedQuantity == originalQuantity) -> true

            // получим список с модиф. товарами
            val modifyGoodsList = state.value.orderEntity.goodsModified

            // удалим из этого списка товар, который больше не является модифицированным
            val resultList = deleteGoodById(
                goodsList = modifyGoodsList,
                idToDelete = commodityId
            )

            // обновляем закэшированый список в репозитории
            orderRepository.updateCashOrderList(
                orderId = state.value.orderId,
                modifyGoodsList = resultList,
            )

            viewModelScope.launch(ioDispatcher) {
                orderRepository.deleteModifyOrderFromDb(
                    orderId = state.value.orderId,
                    commodityId = commodityId
                )
            }

            closeModifiedSheet()
            fetchScreen(orderId = state.value.orderId)
        }
    }

    private fun deleteGoodById(
        goodsList: List<GoodsEntity>,
        idToDelete: String
    ): List<GoodsEntity> {
        return goodsList.filter { it.commodityId != idToDelete }
    }

    fun closeModifiedSheet() {
        _state.value = state.value.copy(
            showModifiedSheet = false
        )
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

    private fun Double.toMyFormat() = String.format(Locale.UK, "%.3f", this)
    private fun Double.toMyFormat_2() = String.format(Locale.UK, "%.2f", this)


    fun onClickArrowBack() {
        navigationService.onClickArrowBack()
    }
}