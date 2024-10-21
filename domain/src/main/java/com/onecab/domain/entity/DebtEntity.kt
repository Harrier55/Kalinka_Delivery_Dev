package com.onecab.domain.entity

data class DebtEntity(
    val accountDocumentNumber:   String = "",
    val amount:                  Double = 0.0,
    val status:                  String = "",
)
