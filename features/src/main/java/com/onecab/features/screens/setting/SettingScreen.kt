package com.onecab.features.screens.setting

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.app_bar.TopAppBarWithMenu
import com.onecab.features.common_component.screenModifier_1
import kotlinx.coroutines.launch

private const val TAG = "SettingScreen"
const val COMMON_WIDTH = 320

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingScreenViewModel? = hiltViewModel()
) {
    Log.i(TAG, "+++ SettingScreen: start")
    val coroutineScope = rememberCoroutineScope()

    val state = viewModel?.state?.collectAsState()?.value

    val login = remember(state) {
        mutableStateOf(state?.login ?: "Иванов Иван Иванович")
    }
    val serverAddress = remember(state) {
        mutableStateOf(state?.server ?: "server")
    }
    val version = remember {
        mutableStateOf(state?.version ?: "1")
    }
    val showExitDialog = remember { mutableStateOf(false) }

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
            Spacer(modifier = modifier.height(50.dp))

            TitleTextBlock(text = "Аккаунт", spacer = 21)

            TextBlock(text1 = "Логин", text2 = login.value, spacer = 30, click = {})

            TitleTextBlock(text = "Настройки подключения", spacer = 20)

            TextBlock(
                text1 = "Адрес сервера",
                text2 = serverAddress.value,
                spacer = 30,
                click = {
                    showExitDialog.value = true
                }
            )

            TitleTextBlock(text = "Приложение", spacer = 20)

            TextBlock(text1 = "Версия", text2 = version.value, spacer = 20, click = {})

            TextBlock(
                text1 = "Сайт разработчика",
                text2 = "https://1cab.ru/",
                spacer = 15,
                click = {})
        }

        ExitDialog(
            openDialog = showExitDialog.value,
            onDismissRequest = { showExitDialog.value = false },
            confirmButton = {
                viewModel?.exitFromProfile()
                showExitDialog.value = false
            }
        )
    }
}

@Composable
fun ExitDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { onDismissRequest.invoke() },
            confirmButton = {
                Button(
                    onClick = {confirmButton.invoke() },
                    content = {Text(text = "Выйти из профиля")}
                )
            },
            title = {
                Text(
                    text = "Изменение адреса сервера",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            text = {
                Text(
                    text = "Для изменения адреса подключения" +
                            " сервера необходимо выполнить выход" +
                            " из профиля пользователя.\n Все регистрационные" +
                            " данные будут удалены"
                )
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun TitleTextBlock(text: String, spacer: Int) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
        modifier = Modifier
            .width(COMMON_WIDTH.dp)
            .padding(bottom = spacer.dp)
    )
}

@Composable
private fun TextBlock(text1: String, text2: String, spacer: Int, click: () -> Unit) {
    Row(
        modifier = Modifier
            .width(COMMON_WIDTH.dp)
            .padding(bottom = spacer.dp)
    ) {
        Text(
            text = text1,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.width(150.dp)

        )

        Text(
            text = text2,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .clickable { click.invoke() }
                .width(COMMON_WIDTH.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewSetting() {
    KalinkaDeliveryTheme {
        SettingScreen(
            viewModel = null
        )
    }
}