package com.onecab.domain.entity

/**
 * Класс для регистрации Неотправленных заказов,
 */

data class UnshippedOrdersEntity(  // 16 полей
    val orderId:             String = "",
    val orderNumber:         String = "",
    val purchaserName:       String = "",
    val paymentDate:         String = "",
    val paymentAmount:       Double = 0.0,

    // флаги доставки
    val doneSbp:             Boolean = false,   // есть ли оплата по банку
    val doneAddPay:          Boolean = false,   // успешен ли метод addOrderPay
    val doneAddDelivery:     Boolean = false,   // успешен ли метод addOrderStatus

    // данные от банка
    val qrd_id_sbp:          String = "",
    val pay_api_info:        String = "",  // информация об оплате, полученная от api банка, т.е. это результат об успешности оплаты

    // остатки от AddOrderPayEntity
    val check_destination:   String = "", // телефон или email для отправки чека
    val check_way:           String = "", // способ оплаты чека (email, sms)
    val pay_sr_info:         String = "", // комментарий, который введен торговым представителем при оплате
    val adjustments_data:    String = "", // изменения товаров

    // остатки от AddOrderStatusEntity
    val delivery_note:       String = "", // комментарий при доставке
    val status:              String = "", // статус доставки
)
