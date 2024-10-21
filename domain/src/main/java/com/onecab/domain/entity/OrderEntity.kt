package com.onecab.domain.entity


data class OrderEntity(
    val purchaserId:         String = "",
    val purchaserName:       String = "",
    val purchaserPhone:      String = "",
    val purchaserEmail:      String = "",
    val orderId:             String = "",
    val orderNumber:         String = "",
    val orderDate:           String = "",
    val invoiceId:           String = "",
    val invoiceRecordId:     String = "",
    val comment:             String = "-",
    val deliveryDate:        String = "",     // дата доставки
    val deliveryAddress:     String = "",     // адрес доставки
    val paymentDate:         String = "",     // дата оплаты
    val paymentAmount:       Double = 0.0,    // сумма оплаты
    val invoiceBarcode:      String = "",     // штрих-код заказа в Base64
    val companyId:           String = "",
    val goodsFromServer:     List<GoodsEntity> = emptyList(),   // оригинальный список товаров в заказе
    val status:              String = "",     // статус доставки

    val goodHasBeenModified: Boolean = false, // флаг-что заказ был модифицирован, в нем изменился количество какого - то товара
    val modifyDate:          String = "",     // соответственно, дата этих изменений в формате yyyyMMdd
    val goodsModified:       List<GoodsEntity> = emptyList(),  // список измененных товаров



)