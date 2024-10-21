package com.onecab.features.screens.authtorization

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.bottom_sheet.ErrorBottomSheet
import com.onecab.features.common_component.buttons.BlueButton
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.common_component.text_input.TextInputFieldOutline

private const val COMMON_WIDTH = 320

@Composable
fun AuthorizationServerScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthorizationServerViewModel? = hiltViewModel()
) {

    val inputAddress = remember { mutableStateOf("") }

    val state = viewModel?.state?.collectAsState()?.value

    val showSheet = remember(state) {
        mutableStateOf(state?.showErrorSheet ?: false)
    }
    val errorText = remember(state) {
        mutableStateOf(state?.textError ?: "")
    }
    val isLoading = remember {
        mutableStateOf(state?.isLoading ?: false)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            // no topBar
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = padding.calculateTopPadding() + 78.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(70.dp))

            TextBlock_20sp(text = "Авторизация")

            TextBlock_16sp(text = "Введите адрес сервера перед вводом \n лoгина и пароля")

            Spacer(modifier = Modifier.height(8.dp))

            TextInputFieldOutline(
                spacerDown = 172.dp,
                width = COMMON_WIDTH.dp,
                placeholder = "Ведите адрес сервера",
                isError = false,
                callback = {
                    inputAddress.value = it
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

            BlueButton(
                textButton = "ПОДКЛЮЧИТЬСЯ",
                widthButton = COMMON_WIDTH,
                enabled = true,
                onClick = {
                    viewModel?.signInServer(address = inputAddress.value)
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

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        AuthorizationServerScreen(viewModel = null)
    }
}