package com.onecab.domain.entity

data class AuthToken(
    val token:       String? = null,   // токен авторизации
    val expiresIn:   Long? = null,     // Время, через которое токен истекает
)