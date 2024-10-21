package com.onecab.domain.entity

/**
 * Сущность для получения параметров настройки с с ервера
 */
data class PaymentParamsEntity(
    val companyId     : String = "",
    val username      : String = "",
    val password      : String = "",
    val merchantIDSBP : String = "",
    val qrTtl         : Int    = 0, // время жизни QR кода (сек)
)
