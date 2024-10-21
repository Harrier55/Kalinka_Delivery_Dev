package com.onecab.features.screens.setting

import androidx.lifecycle.ViewModel
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingScreenUiState(
    val login: String = "",
    val password: String = "",
    val server: String = "",
    val version: String = "",
)

@HiltViewModel
class SettingScreenViewModel @Inject constructor(
    private val localSourceRepository: LocalSourceRepository,
    private val navigationService: NavigationService,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingScreenUiState())
    val state = _state.asStateFlow()

    init {
        fetchScreen()
    }

    private fun fetchScreen() {
        val pass = localSourceRepository.getPassword()

        _state.update {
            SettingScreenUiState(
                login = localSourceRepository.getLogin(),
                password = if (pass.isNotEmpty()) "****" else "",
                server = localSourceRepository.getServerAddress() ?: "",
                version = localSourceRepository.getAppVersion()
            )
        }
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }

    // удаляем данные пользователя и переходим на экран входа
    fun exitFromProfile() {
        // удаляем данные пользователя
        registerRepository.unRegisterUser()

        // навигация
        navigationService.navigateWithoutArgument(route = Screens.AuthorizationServerScreen.route)
    }
}