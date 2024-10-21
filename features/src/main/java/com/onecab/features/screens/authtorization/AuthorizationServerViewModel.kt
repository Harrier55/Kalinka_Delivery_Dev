package com.onecab.features.screens.authtorization

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "AuthorizationServerViewModel"

data class AuthorizationServerScreenUiState(
    val isLoading: Boolean = false,
    val showErrorSheet: Boolean = false,
    val textError: String = ""
)

@HiltViewModel
class AuthorizationServerViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val registerRepository: RegisterRepository,
    private val localSourceRepository: LocalSourceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthorizationServerScreenUiState())
    val state = _state.asStateFlow()

    // ввод адреса сервера и проверка, отвечает ли он
    fun signInServer(address: String) {
        if (address.length > 3) {
            viewModelScope.launch {
                _state.value = state.value.copy(isLoading = true)
                delay(300)
                val responsePingServer = registerRepository.pingServer(address)

                when(responsePingServer){
                    is KalinkaResultResponse.Success -> {
                        // Если сервер отвечает правильно,
                        if (responsePingServer.data == "pong") {
                            _state.value = state.value.copy(isLoading = false)

                            //  сохраняем имя сервера и путь к базе в локальное хранилище
                            val responseSave = localSourceRepository.setServerAddress(
                                address = address
                            )

                            // переход на экран логи/пароль
                            if (responseSave) {
                                navigationService.navigateWithoutArgument(route = Screens.AuthorizationScreen.route)
                            }
                        }
                    }

                    is KalinkaResultResponse.Error -> {
                        _state.value = state.value.copy(
                            textError = "Сервер не отвечает по указанному адресу\nлог ошибки(${responsePingServer.message})",
                            showErrorSheet = true
                        )
                        Log.e(TAG, "--signInServer Error: $responsePingServer")
                    }
                }
            }
        }
    }

    fun closeErrorSheet() {
        _state.value = state.value.copy(showErrorSheet = false)
    }
}