package com.onecab.domain.repository

import com.onecab.domain.entity.AddOrderPayEntity
import com.onecab.domain.entity.AddOrderPayRequestEntity
import com.onecab.domain.entity.AddOrderStatusEntity
import com.onecab.domain.entity.GoodsEntity
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.entity.UnshippedOrdersEntity
import com.onecab.domain.response_entity.KalinkaResultResponse

interface OrderRepository {

    suspend fun getOrdersFromServer(date: String, token: String): List<OrderEntity>
    fun getOrdersFromCash(): List<OrderEntity>
    fun getOrderByIdFromCash(orderId: String): OrderEntity
    fun getOrderByBarCode(invoiceBarcode: String): OrderEntity

    fun updateCashOrderList(orderId: String, modifyGoodsList: List<GoodsEntity>)
    fun updateCashOrderList(ordersList: List<OrderEntity>)

    /*---- Работа с завершенными заказами ---*/

    fun setCompleteOrdersToCash(completeOrders: List<OrderEntity>)
    fun getCompleteOrdersFromCash(): List<OrderEntity>
    fun getCompleteOrderByIdFromCash(orderId: String): OrderEntity

    /*------работа с модифицированными товарами ------------------*/

    // записать модифицированный заказ в БД
    suspend fun saveModifyOrderToDb(
        orderId: String,
        commodityId: String,
        quantity: Double,
        modifyDate: String
    ): Boolean

    suspend fun getModifyOrderFromDb(): List<OrderEntity>
    // удалить модифицированный товар из базы
    suspend fun deleteModifyOrderFromDb(orderId: String, commodityId: String)

    /*------------------------------------------------------------*/

    // счетчик количества созданных экземпляров
    fun setCountInstance()
    fun isFirstInstance(): Boolean
    fun resetCountInstance()

    // отправить заказ на сервер , как оплаченный
    suspend fun addOrderPay(
        token: String,
        addOrderPayEntity: AddOrderPayEntity
    ): KalinkaResultResponse<Boolean>

    // отправить заказ на сервер , как доставленный
    suspend fun addOrderStatus(
        token: String,
        addOrderStatusEntity: AddOrderStatusEntity
    ): KalinkaResultResponse<Boolean>

    // отправить ответ об оплате СБП на наш сервер
    suspend fun addOrderPayRequest(
        token: String,
        addOrderPayRequestEntity: AddOrderPayRequestEntity
    ): Boolean

    // записать заказ, ожидающий загрузку в БД
    suspend fun saveUnshippedOrderToDb(unshippedOrdersEntity: UnshippedOrdersEntity): Boolean

    // получить заказы из бвзы ожидания
    suspend fun getUnshippedOrderFromDb(): List<UnshippedOrdersEntity>

    // обновить заказ в базе ожидания
    suspend fun updateUnshippedOrderFromDb(unshippedOrdersEntity: UnshippedOrdersEntity): Boolean

    // удалить заказ из базы ожидания
    suspend fun deleteUnshippedOrderFromDb(unshippedOrdersEntity: UnshippedOrdersEntity): Boolean
}