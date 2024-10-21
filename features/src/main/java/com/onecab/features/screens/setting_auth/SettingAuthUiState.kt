package com.onecab.features.screens.setting_auth

import androidx.compose.runtime.Stable

@Stable
data class SettingAuthUiState(
    val serverAddress: String = "",
    val version: String = "",
    val showEditAddressSheet: Boolean = false,
    val showDialogSuccess: Boolean = false,
)