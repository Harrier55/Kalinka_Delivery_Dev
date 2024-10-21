package com.onecab.domain.bank_entity

data class NewQRRequest(
    val Merchant_ID_SBP:     String,    // Идентификатор Точки продажи в СБП
    val QR_Type:             String,    // Тип QR кода: 02 — QRD (одноразовый — динамический
    val Payment_Amount:      Int,       // Сумма платежа в копейках
    val qrTtl:               Int,       // Срок жизни ссылки QRD в минутах
    val image:               ImageData
)

data class ImageData(
    val mediaType:   String,    // тип картинки (image/png)
    val width:       Int,       // ширина
    val height:      Int        // высота
)