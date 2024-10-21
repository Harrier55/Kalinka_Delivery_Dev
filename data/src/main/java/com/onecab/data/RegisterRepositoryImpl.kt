package com.onecab.data

import android.util.Log
import com.onecab.domain.entity.AuthToken
import com.onecab.domain.enums.getErrorName
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.domain.response_entity.KalinkaResultResponse
import com.onecab.source_web.api.RegisterService
import com.onecab.source_web.auth_dto.AuthRequestDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "RegisterRepositoryImpl"

@Singleton
class RegisterRepositoryImpl @Inject constructor(
    private val localSourceRepository: LocalSourceRepository
) : RegisterRepository {

    // эмитирует значение зарегистрирован пользователь или нет - используется для логики меню
    // значение отправляется сразу в mainActivity, потом может что-то изменим
    private val _userIsRegistered = MutableStateFlow(false)
    override val userIsRegistered: StateFlow<Boolean> = _userIsRegistered

    // кэшируем значение токена
    private var cashAuthToKen = AuthToken()

    // Аутентифицируем юзера и получаем токен, URL получаем из Preference
    override suspend fun signInUser(login: String, password: String): Result<Boolean> {
        return try {
            val baseUrl = localSourceRepository.getServerAddress() ?: "нет данных"
            val apiService = RegisterService().getRegisterServiceApi(inputUrl = baseUrl)
            val response = apiService.login(
                authRequestDto = AuthRequestDTO(
                    login = login,
                    hashSum = password
                )
            )
            Log.d(TAG, "-- signInUser: getAuthToken response =  $response")
            cashAuthToKen = AuthToken(token = response, expiresIn = 0) // кэшируем токен
            _userIsRegistered.update { true }   // для переключения статуса в DrawerMenu
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "-- signInUser: getAuthToken response error = $e")
            Result.failure(e)
        }
    }

    override fun getAuthToken(): AuthToken {
        return cashAuthToKen
    }

    // пользователь вводит адрес сервера, мы проверяем, отвечает ли он
    override suspend fun pingServer(url: String): KalinkaResultResponse<String> {
        try {
            val registerApi = RegisterService().getRegisterServiceApi(inputUrl = url)
            val response = registerApi.ping()

            Log.d(TAG, "-- pingServer response =  $response")

            return when (response.code()) {
                200 -> {
                    KalinkaResultResponse.Success(data = response.body() ?: "")
                }

                else -> {
                    KalinkaResultResponse.Error(
                        httpCode = response.code(),
                        message = getErrorName(code = response.code())?: response.message()
                    )
                }
            }

        } catch (e: Exception) {
            Log.w(TAG, "-- pingServer Exception: ", e)
            return KalinkaResultResponse.Error(httpCode = 0, message = e.message ?: "Exception")
        }
    }


    override fun registerUser(login: String, password: String): Boolean {
        //todo Not yet implemented
        return true
    }

    // удалить данные пользователя
    override fun unRegisterUser(): Boolean {
        _userIsRegistered.update { false }
        return localSourceRepository.removeUserData()
    }
}