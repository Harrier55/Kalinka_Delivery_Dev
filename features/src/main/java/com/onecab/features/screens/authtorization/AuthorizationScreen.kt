package com.onecab.features.screens.authtorization

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.app_bar.TopAppBarWithMenu
import com.onecab.features.common_component.bottom_sheet.ErrorBottomSheet
import com.onecab.features.common_component.buttons.BlueButton
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.common_component.text_input.TextInputFieldOutline
import kotlinx.coroutines.launch

private const val COMMON_WIDTH = 320

/*
    У экрана есть определенная логика.
    Если анреса сервера еще нет, например вход выполняется первый раз,
    то показываем экран ввода адреса сервера
    А если адрес сервера уже есть, то показываем экран ввода логин - пароль
    переменная val serverAddressExists - отвечает за логику выбора экранов
 */

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthorizationScreenViewModel? = hiltViewModel()
) {
    Log.i("AuthorizationScreen", "+++ AuthorizationScreen: start")
    val coroutineScope = rememberCoroutineScope()
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }
    val showSheet = remember { mutableStateOf(false) }

    viewModel?.let { vm ->
        isError.value = vm.isError.collectAsState().value
        isLoading.value = vm.isLoading.collectAsState().value
        errorText.value = vm.errorText.collectAsState().value
        showSheet.value = vm.openSheet.collectAsState().value
        login.value = vm.login.collectAsState().value
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithMenu(
                textAppBar = "",
                onClickMenu = {
                    coroutineScope.launch {
                        viewModel?.openNavigationDrawer()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = padding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(70.dp))

            TextBlock_20sp(text = "Авторизация")

            TextBlock_16sp(text = "Введите логин и пароль для входа")

            Spacer(modifier = Modifier.height(30.dp))

            TextInputFieldOutline(
                spacerDown = 30.dp,
                width = COMMON_WIDTH.dp,
                placeholder = if (login.value.isEmpty()) "Логин" else login.value,
                isError = false,
                callback = {
                    login.value = it
                }
            )

            TextInputFieldOutline(
                spacerDown = 100.dp,
                width = COMMON_WIDTH.dp,
                placeholder = "Пароль",
                isError = false,
                callback = {
                    password.value = it
                }
            )

            Box(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .size(40.dp)
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 3.dp
                    )
                }
            }

            if (isError.value) {
                Text(
                    text = "Неверный логин или пароль",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            BlueButton(
                textButton = "ВОЙТИ",
                widthButton = COMMON_WIDTH,
                enabled = true,
                onClick = {
                    viewModel?.handlerButtonSignInUser(
                        login = login.value,
                        password = password.value
                    )
                }
            )
        }
    }

    // Шторка отображения ошибок
    ErrorBottomSheet(
        openSheet = showSheet.value,
        closeSheet = { viewModel?.closeErrorSheet() },
        title = "Произошла ошибка при обращении к серверу",
        textError = errorText.value
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    KalinkaDeliveryTheme {
        AuthorizationScreen(viewModel = null)
    }
}