package com.onecab.features.screens.setting_auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.navigation_api.NavigationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingAuthScreenViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val localSourceRepository: LocalSourceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingAuthUiState())
    val state = _state.asStateFlow()

    fun fetchScreen() {
        _state.update {
            state.value.copy(
                serverAddress = localSourceRepository.getServerAddress() ?: "null",
                version = localSourceRepository.getAppVersion()
            )
        }
    }

    suspend fun openNavigationDrawer() {
        navigationService.openNavigationDrawer()
    }

    fun editServerAddress(address: String) {
        viewModelScope.launch {
            closeSheet()
            openDialog()
            localSourceRepository.setServerAddress(address = address)
            delay(500)
            closeSheet()
            closeDialog()
            fetchScreen()  // обновить экран после редактирования
        }
    }

    fun openSheet() {
        _state.update {
            state.value.copy(
                showEditAddressSheet = true
            )
        }
    }

    fun closeSheet() {
        _state.update {
            state.value.copy(
                showEditAddressSheet = false
            )
        }
    }

    private fun openDialog() {
        _state.update {
            state.value.copy(
                showDialogSuccess = true
            )
        }
    }

    fun closeDialog() {
        _state.update {
            state.value.copy(
                showDialogSuccess = false
            )
        }
    }
}