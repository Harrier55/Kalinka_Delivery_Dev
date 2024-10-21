package com.onecab.features.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.domain.response_entity.KalinkaResultResponse
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val localSourceRepository: LocalSourceRepository,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    fun authorizationStatus() {

        val serverAddress = localSourceRepository.getServerAddress()
        val userName = localSourceRepository.getLogin()
        val password = localSourceRepository.getPassword()

        viewModelScope.launch {
            delay(500)

            if (addressServerExist()) {
                // Делаем ping на сервер, отвечает ли он
                val serverPingResult = signInServer(address = serverAddress!!)

                if (serverPingResult) {  // Сеовер отвечает на запрос
                    if (userName.isNotEmpty() && password.isNotEmpty()) {
                        val resultAuth = registerRepository
                            .signInUser(
                                login = userName,
                                password = password.toMD5()
                            )

                        resultAuth.fold(
                            onSuccess = {
                                navigationService.navigateWithoutArgument(route = Screens.OrdersListScreen.route)
                            },
                            onFailure = { tr ->
                                navigationService.navigateWithoutArgument(route = Screens.AuthorizationScreen.route)
                            }
                        )

                    } else {
                        navigationService.navigateWithoutArgument(route = Screens.AuthorizationScreen.route)
                    }

                } else {
                    navigationService.navigateWithoutArgument(route = Screens.AuthorizationServerScreen.route)
                }

            } else {
                navigationService.navigateWithoutArgument(route = Screens.AuthorizationServerScreen.route)
            }
        }
    }

    // ввод адреса сервера и проверка, отвечает ли он
    private suspend fun signInServer(address: String): Boolean {
        val responsePingServer = registerRepository.pingServer(address)

        when (responsePingServer) {
            is KalinkaResultResponse.Success -> {
                // Если сервер отвечает правильно,
                if (responsePingServer.data == "pong") {
                    Log.d("SplashScreenVM", "--signInServer Адрес $address ответ pong")
                    return true
                } else {
                    Log.d("SplashScreenVM", "--signInServer Адрес $address ответ Не pong")
                    return false
                }
            }

            is KalinkaResultResponse.Error -> {
                Log.d(
                    "SplashScreenVM",
                    "--Адрес -- $address Статус false__error ${responsePingServer.message}"
                )
                return false
            }
        }
    }


    // проверка, существует ли имя сервера в локальном хранилище
    private fun addressServerExist(): Boolean {
        val serverAddress = localSourceRepository.getServerAddress()

        return if (serverAddress != null && serverAddress.length > 3) {
            Log.d("SplashScreenVM", "--addressServerExist -- $serverAddress Статус true")
            true
        } else {
            Log.d("SplashScreenVM", "--addressServerExist  -- $serverAddress Статус false")
            false
        }
    }

    private fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}