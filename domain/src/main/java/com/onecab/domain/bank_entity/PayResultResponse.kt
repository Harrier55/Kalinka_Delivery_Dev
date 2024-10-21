package com.onecab.domain.bank_entity

sealed class PayResultResponse<out T>{
    data class Success<T>(val data: T): PayResultResponse<T>()
    data class Error(val httpCode: Int, val message: String): PayResultResponse<Nothing>()
}