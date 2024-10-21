package com.onecab.features.screens.setting_auth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.alert_dialogs.DialogSuccess
import com.onecab.features.common_component.app_bar.TopAppBarWithMenu
import com.onecab.features.common_component.screenModifier_1
import kotlinx.coroutines.launch

private const val COMMON_WIDTH = 320

@Composable
fun SettingAuthScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingAuthScreenViewModel? = hiltViewModel()
) {
    Log.i("SettingAuthScreen", "SettingAuthScreen: start")
    val coroutineScope = rememberCoroutineScope()
    val serverAddress = remember { mutableStateOf("test/office") }
    val version = remember { mutableStateOf("test 0000") }

    // flags
    val showEditAddressSheet = remember { mutableStateOf(false) }
    val showDialogSuccess = remember { mutableStateOf(false) }

    viewModel?.let { vm ->
        val state = vm.state.collectAsState().value

        serverAddress.value = state.serverAddress
        version.value = state.version
        showDialogSuccess.value = state.showDialogSuccess
        showEditAddressSheet.value = state.showEditAddressSheet
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithMenu(
                textAppBar = "Настройки",
                onClickMenu = {
                    coroutineScope.launch { viewModel?.openNavigationDrawer() }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Настройки подключения",
                style = MaterialTheme.typography.bodyLarge
                    .copy(color = Color.Black),
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 40.dp, bottom = 20.dp)
            )

            Row(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
            ) {
                Text(
                    text = "Адрес сервера",
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = Color.Black),
                )

                Text(
                    text = serverAddress.value,
                    style = MaterialTheme.typography.labelLarge
                        .copy(color = Color.Black, textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .clickable {
                            viewModel?.openSheet()
                        }
                )
            }

            Text(
                text = "Приложение",
                style = MaterialTheme.typography.bodyLarge
                    .copy(color = Color.Black),
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 40.dp, bottom = 20.dp)
            )

            Row(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
            ) {
                Text(
                    text = "Версия",
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.surface),
                )

                Text(
                    text = version.value,
                    style = MaterialTheme.typography.labelLarge
                        .copy(color = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.padding(start = 95.dp)
                )
            }

            Row(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Сайт разработчика",
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.surface),
                )

                Text(
                    text = "abmobile.ru",
                    style = MaterialTheme.typography.labelLarge
                        .copy(
                            color = MaterialTheme.colorScheme.tertiary,
                            textDecoration = TextDecoration.Underline
                        ),
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }

        // EditServerAddress sheet
        EditServerAddressBottomSheet(
            inputAddress = serverAddress.value,
            openSheet = showEditAddressSheet.value,
            closeSheet = { viewModel?.closeSheet() },
            callBack = { viewModel?.editServerAddress(address = it) }
        )

        // Success dialog
        DialogSuccess(
            openDialog = showDialogSuccess.value,
            textDialog = "Адрес сервера обновлен",
            onCloseDialog = { viewModel?.closeDialog() }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        SettingAuthScreen(
            viewModel = null
        )
    }
}