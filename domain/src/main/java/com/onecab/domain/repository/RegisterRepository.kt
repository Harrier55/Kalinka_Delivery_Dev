package com.onecab.domain.repository

import com.onecab.domain.entity.AuthToken
import com.onecab.domain.response_entity.KalinkaResultResponse
import kotlinx.coroutines.flow.StateFlow

interface RegisterRepository {

    val userIsRegistered: StateFlow<Boolean>
    fun registerUser(login: String, password: String): Boolean
    fun unRegisterUser(): Boolean
    fun getAuthToken(): AuthToken
    suspend fun signInUser(login: String, password: String): Result<Boolean>
    suspend fun pingServer(url: String): KalinkaResultResponse<String>
}