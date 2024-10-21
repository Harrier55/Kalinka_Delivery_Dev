package com.onecab.domain.bank_entity

data class StatusQrdEntity(
    val paymentStatus:   String = "",   // статус ответа ACWP и пр.
    val paymentIdBank:   String = "",   // Идентификатор операции, инициированной QR-кодом. Формируется Банком
    val timeStamp:       String = "",   // Дата и время выполнения операции
    val payerIdSBP:      String = "",   // Маскированный № телефона плательщика
)
