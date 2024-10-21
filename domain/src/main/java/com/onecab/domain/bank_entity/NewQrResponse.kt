package com.onecab.domain.bank_entity

data class NewQrResponse(
    val QRDIDSBP:        String? = null,    // ID QR-кода в СБП
    val payload:         String? = null,    // Платежный URL для формирования QR-кода
    val imageContent:    String? = null     // картинка кода в Base64
)
