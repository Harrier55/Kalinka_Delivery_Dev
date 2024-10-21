package com.onecab.domain.response_entity

sealed class KalinkaResultResponse <out T> {
    data class Success<T>(val data: T): KalinkaResultResponse<T>()
    data class Error(val httpCode: Int, val message: String): KalinkaResultResponse<Nothing>()
}