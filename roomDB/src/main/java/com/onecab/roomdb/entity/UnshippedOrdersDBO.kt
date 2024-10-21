package com.onecab.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  Условно это база данных ожидания
 *  здесь хранятся заказы, которые были оплачены, но по каким-то причинам
 *  еще не отправлены на наш сервер
 *
 *  этот объект объединяет в себе AddOrderPayEntity и AddOrderStatusEntity
 */

@Entity(tableName = "unshipped_orders")
data class UnshippedOrdersDBO(
    @PrimaryKey
    @ColumnInfo(name = "order_id")               val orderId: String,
    @ColumnInfo(name = "qrd_id_sbp")             val qrd_id_sbp: String,
    @ColumnInfo(name = "order_number")           val orderNumber: String,
    @ColumnInfo(name = "purchaser_name")         val purchaserName: String,
    @ColumnInfo(name = "payment_date")           val paymentDate: String,
    @ColumnInfo(name = "payment_amount")         val paymentAmount: Double,
    @ColumnInfo(name = "done_sbp")               val doneSbp: Boolean,
    @ColumnInfo(name = "done_add_pay")           val doneAddPay: Boolean,
    @ColumnInfo(name = "done_add_delivery")      val doneAddDelivery: Boolean,
    @ColumnInfo(name = "pay_api_info")           val pay_api_info: String,

    // остатки от AddOrderPayEntity
    @ColumnInfo(name = "check_destination")     val check_destination: String,
    @ColumnInfo(name = "check_way")             val check_way: String,
    @ColumnInfo(name = "pay_sr_info")           val pay_sr_info: String,
    @ColumnInfo(name = "adjustments_data")      val adjustments_data: String,

    // остатки от AddOrderStatusEntity
    @ColumnInfo(name = "delivery_note")         val delivery_note: String,
    @ColumnInfo(name = "status")                val status: String,
)

