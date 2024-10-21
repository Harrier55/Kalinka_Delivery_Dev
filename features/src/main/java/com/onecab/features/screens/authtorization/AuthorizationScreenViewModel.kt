package com.onecab.features.screens.authtorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AuthorizationScreenViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val registerRepository: RegisterRepository,
    private val localSourceRepository: LocalSourceRepository
) : ViewModel() {

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorText = MutableStateFlow("")
    val errorText = _errorText.asStateFlow()

    private val _openSheet = MutableStateFlow(false)
    val openSheet = _openSheet.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    init {
        _login.value = localSourceRepository.getLogin()
    }

    fun handlerButtonSignInUser(
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.update { true }
            delay(500)
            val result = registerRepository
                .signInUser(
                    login = login,
                    password = password.toMD5()
                )

            result.fold(
                onSuccess = {
                    _isLoading.update { false }
                    localSourceRepository.saveLogin(login = login)
                    localSourceRepository.savePassword(pass = password)
                    navigationService.navigateWithoutArgument(route = Screens.OrdersListScreen.route)
                },
                onFailure = { tr ->
                    openErrorSheet("Error authorization: ${tr.message}")
                }
            )
        }
    }

    private fun openErrorSheet(error: String) {
        _errorText.update { error }
        _isLoading.update { false }
        _openSheet.update { true }
    }

    fun closeErrorSheet() {
        _openSheet.update { false }
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }

    private fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}