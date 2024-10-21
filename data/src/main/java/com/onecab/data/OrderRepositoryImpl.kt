package com.onecab.data

import android.util.Log
import com.onecab.domain.entity.AddOrderPayEntity
import com.onecab.domain.entity.AddOrderPayRequestEntity
import com.onecab.domain.entity.AddOrderStatusEntity
import com.onecab.domain.entity.GoodsEntity
import com.onecab.domain.entity.OrderEntity
import com.onecab.domain.entity.UnshippedOrdersEntity
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.response_entity.KalinkaResultResponse
import com.onecab.roomdb.dao.OrderDAO
import com.onecab.roomdb.entity.ModifiedOrdersDBO
import com.onecab.roomdb.entity.UnshippedOrdersDBO
import com.onecab.source_web.api.KalinkaApiService
import com.onecab.source_web.models_dto.AddOrderPayDTO
import com.onecab.source_web.models_dto.AddOrderPayRequestDTO
import com.onecab.source_web.models_dto.AddOrderStatusDTO
import com.onecab.source_web.models_dto.GoodsDTO
import com.onecab.source_web.models_dto.OrderDTO
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "OrderRepositoryImpl"

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val kalinkaApiService: KalinkaApiService,
    private val orderDAO: OrderDAO
) : OrderRepository {

    // кэшируем несколько параметров
    private var cashOrdersList = mutableListOf<OrderEntity>()
    private var cashCompleteOrders = mutableListOf<OrderEntity>()

    // Если в процессе заказа количество какого-то товара было изменено
    // то вставляем эти товары в требуемый заказ и обновляем кэш списка заказов
    override fun updateCashOrderList(
        orderId: String,
        modifyGoodsList: List<GoodsEntity>
    ) {
        // формируем список с заказма, где товары были модифицированы
        // добавляем товары, которые были изменены в свой список
        // и обновляем кэш
        val newList = cashOrdersList.map { orderEntity ->
            if (orderEntity.orderId == orderId) {
                orderEntity.copy(
                    goodHasBeenModified = modifyGoodsList.isNotEmpty(),
                    modifyDate = orderEntity.modifyDate,
                    goodsModified = modifyGoodsList
                )
            } else {
                orderEntity
            }
        }
        // и обновляем кэш
        cashOrdersList = newList.toMutableList()
    }

    // Обновляем кэш списка заказов
    override fun updateCashOrderList(ordersList: List<OrderEntity>) {
        cashOrdersList = ordersList.toMutableList()
    }

    // Сохранить товар в базу данных
    override suspend fun saveModifyOrderToDb(
        orderId: String,
        commodityId: String,
        quantity: Double,
        modifyDate: String
    ): Boolean {
        try {
            orderDAO.saveGoods(
                ModifiedOrdersDBO(
                    orderId = orderId,
                    commodityId = commodityId,
                    quantity = quantity,
                    modifyDate = modifyDate,
                    amount = 0.0,
                    vatAmount = 0.0
                )
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    // Получить список заказов из БД, в которых происходили изменения в каких-то товарах
    override suspend fun getModifyOrderFromDb(): List<OrderEntity> {
        Log.d(TAG, ">>> Выполнен запрос на в базу модифицированных товаров")

        // Здесь получим список товаров из БД и преобразовываем его в список заказов
        try {
            val listDbo = orderDAO.getGoods()

            val groupList = listDbo.groupBy { it.orderId }

            val modifyOrdersList = mutableListOf<OrderEntity>()

            for (order in groupList) {
                val newOrderId = order.key
                val newGoodsList = mutableListOf<GoodsEntity>()

                order.value.forEach { modifiedOrdersDBO ->
                    newGoodsList.add(
                        GoodsEntity(
                            commodityId = modifiedOrdersDBO.commodityId,
                            goodHasBeenModified = true,
                            modifyDate = modifiedOrdersDBO.modifyDate,
                            modifyQuantity = modifiedOrdersDBO.quantity,
                        )
                    )
                }

                modifyOrdersList.add(
                    OrderEntity(
                        orderId = newOrderId,
                        goodHasBeenModified = true,
                        goodsModified = newGoodsList
                    )
                )
            }

            return modifyOrdersList

        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    // удалить запись о модифицированом товаре из базы
    override suspend fun deleteModifyOrderFromDb(orderId: String, commodityId: String) {
        try {
            orderDAO.deleteGoods(
                modifiedOrdersDBO = ModifiedOrdersDBO(
                    orderId = orderId,
                    commodityId = commodityId,
                    quantity = 0.0,
                    modifyDate = "",
                    amount = 0.0,
                    vatAmount = 0.0
                )
            )
            Log.d(TAG, "Запись из базы модифицированных заказов удалена")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Удаление модифицированного заказа -  Error")
        }
    }

    // Получить список заказов с сервера по дате. Не кэшируем. Дата д/б в формате yyyyMMdd Важно !
    override suspend fun getOrdersFromServer(date: String, token: String): List<OrderEntity> {
        Log.d(TAG, ">>> Выполнен запрос на сервер с датой $date")
        try {
            val response = kalinkaApiService
                .getOrders(
                    token = token,
                    date = date
                )

            return response
                .toList()
                .map { orderDTO ->
                    orderDTO.toOrderEntity()
                }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "---- getOrdersFromServer: Error  ${e.message}")
            return emptyList()
        }
    }

    // получить список заказов из кэша
    override fun getOrdersFromCash(): List<OrderEntity> {
        return cashOrdersList
    }

    // Получить информацию о заказе ( из кэша )
    override fun getOrderByIdFromCash(orderId: String): OrderEntity {
        cashOrdersList
            .forEach { entity ->
                if (entity.orderId == orderId) return entity
            }
        return OrderEntity()
    }

    // Получить информацию о заказе ( из кэша ) по штрих коду
    override fun getOrderByBarCode(invoiceBarcode: String): OrderEntity {
        cashOrdersList
            .forEach { entity ->
                if (entity.invoiceBarcode == invoiceBarcode) return entity
            }
        return OrderEntity()
    }

    /* -------------- Завеншенные заказы ---------------------*/

    // записать список завершенных заказов в кэш
    override fun setCompleteOrdersToCash(completeOrders: List<OrderEntity>) {
        cashCompleteOrders.clear()
        cashCompleteOrders = completeOrders.toMutableList()
    }

    // получить список завершенных заказов из кэша
    override fun getCompleteOrdersFromCash(): List<OrderEntity> {
        return cashCompleteOrders
    }

    // Получить информацию о завершенном заказе (из кэша )
    override fun getCompleteOrderByIdFromCash(orderId: String): OrderEntity {
        cashCompleteOrders
            .forEach { entity ->
                if (entity.orderId == orderId) return entity
            }
        return OrderEntity()
    }

    // Для реализации логики только однократной автоматической загрузки первого экрана (Главного)
    // нужен счетчик количества экземпляров
    private var countInstance = 1

    override fun setCountInstance() {
        countInstance++
    }

    // если загрузка экрана  первая - вернем true
    override fun isFirstInstance(): Boolean {
        return countInstance <= 1
    }

    // сбросить счетчик загрузок
    override fun resetCountInstance() {
        countInstance = 1
    }

    /* -------- Отправка заказов на сервер ---------------*/

    // оправим оплаченный заказ на сервер
    override suspend fun addOrderPay(
        token: String,
        addOrderPayEntity: AddOrderPayEntity
    ): KalinkaResultResponse<Boolean> {
        try {
            val response = kalinkaApiService.addOrderPay(
                token = token,
                addOrderPayDTO = addOrderPayEntity.toAddOrderPayDTO()
            )
            return when (response.code()) {
                200 -> {
                    KalinkaResultResponse.Success(
                        data = true
                    )
                }

                else -> {
                    KalinkaResultResponse.Error(
                        httpCode = response.code(),
                        message = response.message()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return KalinkaResultResponse.Error(
                httpCode = 0,
                message = e.message!!
            )
        }
    }

    // оправим заказ на сервер, как доставленный
    override suspend fun addOrderStatus(
        token: String,
        addOrderStatusEntity: AddOrderStatusEntity
    ): KalinkaResultResponse<Boolean> {
        try {
            val response = kalinkaApiService.addOrderStatus(
                token = token,
                addOrderStatusDTO = addOrderStatusEntity.toAddOrderStatusDTO()
            )
            return when (response.code()) {
                200 -> {
                    KalinkaResultResponse.Success(
                        data = true
                    )
                }

                else -> {
                    KalinkaResultResponse.Error(
                        httpCode = response.code(),
                        message = response.message()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return KalinkaResultResponse.Error(
                httpCode = 0,
                message = e.message!!
            )
        }
    }

    // // отправить на сервер результат получения QR кода
    override suspend fun addOrderPayRequest(
        token: String,
        addOrderPayRequestEntity: AddOrderPayRequestEntity
    ): Boolean {
        try {
            val response = kalinkaApiService.addOrderPayRequest(
                token = token,
                addOrderPayRequestDTO = addOrderPayRequestEntity.toAddOrderPayRequestDTO()
            )
            Log.d(TAG, " ---- addOrderPayRequest: $response")
            return when (response.code()) {
                200 -> true
                else -> false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /*--------------- Работа с заказами, ожидающими загрузки -----------*/

    // Записать заказ, ожидающий загрузку на наш сервер в базу данных для ожидающий
    override suspend fun saveUnshippedOrderToDb(
        unshippedOrdersEntity: UnshippedOrdersEntity
    ): Boolean {
        try {
            val response = orderDAO.saveUnshippedOrder(
                unshippedOrdersDBO = unshippedOrdersEntity.toUnshippedOrdersDBO()
            )
            Log.d(TAG, "--saveUnshippedOrderToDb response = $response")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    // Получить заказы из базы ожидания
    override suspend fun getUnshippedOrderFromDb(): List<UnshippedOrdersEntity> {
        try {
            return orderDAO.getUnshippedOrders()
                .map { unshippedOrdersDBO ->
                    unshippedOrdersDBO.toUnshippedOrdersEntity()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    // обновить заказ в базе ожидания
    override suspend fun updateUnshippedOrderFromDb(unshippedOrdersEntity: UnshippedOrdersEntity): Boolean {
        try {
            orderDAO.updateUnshippedOrder(
                unshippedOrdersDBO = unshippedOrdersEntity.toUnshippedOrdersDBO()
            )
            Log.d(TAG, "updateUnshippedOrderFromDb: Заказ успешно обновлен")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    // удалить заказ из базы ожидания
    override suspend fun deleteUnshippedOrderFromDb(unshippedOrdersEntity: UnshippedOrdersEntity): Boolean {
        try {
            orderDAO.deleteUnshippedOrder(
                unshippedOrdersDBO = unshippedOrdersEntity.toUnshippedOrdersDBO()
            )
            Log.d(TAG, "deleteUnshippedOrderFromDb: Заказ успешно Удален")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /* ----------------  Функции расширения --------------*/

    private fun AddOrderPayRequestEntity.toAddOrderPayRequestDTO(): AddOrderPayRequestDTO {
        return AddOrderPayRequestDTO(
            orderId = this.orderId,
            operationDate = this.operationDate,
            operationAmount = this.operationAmount,
            qrdIdSbp = this.qrdIdSbp,
            sbpRequest = this.sbpRequest,
            sbpResult = this.sbpResult,
        )
    }


    private fun UnshippedOrdersDBO.toUnshippedOrdersEntity(): UnshippedOrdersEntity {
        return UnshippedOrdersEntity(
            orderId = this.orderId,
            qrd_id_sbp = this.qrd_id_sbp,
            orderNumber = this.orderNumber,
            purchaserName = this.purchaserName,
            paymentAmount = this.paymentAmount,
            paymentDate = this.paymentDate,
            pay_api_info = this.pay_api_info,
            doneSbp = this.doneSbp,
            doneAddPay = this.doneAddPay,
            doneAddDelivery = this.doneAddDelivery,
            check_destination = this.check_destination,
            check_way = this.check_way,
            pay_sr_info = this.pay_sr_info,
            adjustments_data = this.adjustments_data,
            delivery_note = this.delivery_note,
            status = this.status,

            )
    }

    private fun UnshippedOrdersEntity.toUnshippedOrdersDBO(): UnshippedOrdersDBO {
        return UnshippedOrdersDBO(
            orderId = this.orderId,
            qrd_id_sbp = this.qrd_id_sbp,
            orderNumber = this.orderNumber,
            purchaserName = this.purchaserName,
            paymentDate = this.paymentDate,
            paymentAmount = this.paymentAmount,
            pay_api_info = this.pay_api_info,
            doneSbp = this.doneSbp,
            doneAddPay = this.doneAddPay,
            doneAddDelivery = this.doneAddDelivery,
            check_destination = this.check_destination,
            check_way = this.check_way,
            pay_sr_info = this.pay_sr_info,
            adjustments_data = this.adjustments_data,
            delivery_note = this.delivery_note,
            status = this.status,
        )
    }

    private fun AddOrderStatusEntity.toAddOrderStatusDTO(): AddOrderStatusDTO {
        return AddOrderStatusDTO(
            order_id = this.order_id,
            delivery_date = this.delivery_date,
            delivery_note = this.delivery_note,
            status = this.status
        )
    }

    private fun AddOrderPayEntity.toAddOrderPayDTO(): AddOrderPayDTO {
        return AddOrderPayDTO(
            order_id = this.order_id,
            pay_date = this.pay_date,
            pay_amount = this.pay_amount,
            check_destination = this.check_destination,
            check_way = this.check_way,
            pay_api_info = this.pay_api_info,
            pay_sr_info = this.pay_sr_info,
            adjustments_data = this.adjustments_data
        )
    }

    private fun GoodsDTO.toGoodsEntity(): GoodsEntity {
        return GoodsEntity(
            commodityId = this.commodityId ?: "",
            commodityName = this.commodityName ?: "",
            unitName = this.unitName ?: "",
            quantity = this.quantity ?: 0.0,
            price = this.price ?: 0.0,
            amount = this.amount ?: 0.0,
            vatAmount = this.vatAmount ?: 0.0
        )
    }

    private fun OrderDTO.toOrderEntity(): OrderEntity {
        return OrderEntity(
            purchaserId = this.purchaserId ?: "",
            purchaserName = this.purchaserName ?: "",
            purchaserPhone = this.purchaserPhonenumber ?: "",
            purchaserEmail = this.purchaserEmail ?: "",
            orderId = this.orderId ?: "",
            orderNumber = this.orderNumber ?: "",
            orderDate = this.orderDate ?: "",
            invoiceId = this.invoiceId ?: "",
            invoiceRecordId = this.invoicerecordId ?: "",
            comment = this.extra ?: "",
            deliveryDate = this.deliveryDate ?: "",
            deliveryAddress = this.deliveryAddress ?: "",
            paymentDate = this.paymentDate ?: "",
            paymentAmount = this.paymentAmount ?: 0.0,
            invoiceBarcode = this.invoiceBarcode ?: "",
            companyId = this.companyId?: "",
            goodsFromServer = this.goods
                .map { goodsDTO -> goodsDTO.toGoodsEntity() },
            status = this.status ?: "",
        )
    }
}


