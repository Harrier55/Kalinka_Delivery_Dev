package com.onecab.domain.entity

/** Сущность для регистрации QR кодов в базе данных */

data class QrCodeCheckerEntity(

    val qrdIdSbp:            String,
    val orderId:             String,
    val paymentAmount:       Double,
    val date:                String,    // 2024-09-27
    val dateTime:            String,    // yyyy-MM-dd'T'HH:mm:ss
    val status:              String,    // статус оплаты по QR коду
    val image:               String,    // картинка QR кода в формате Base64
)
