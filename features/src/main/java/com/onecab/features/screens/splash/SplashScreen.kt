package com.onecab.features.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme

/**
    В экране логика - проверяем, если адрес сервера уже есть в локальном хранилище
    то есть уже ранее вносился - то переключается на экран ввода логина/пароля
    если адрес еще не вводился  - на экран ввода адреса сервера.

    Далее проверяем - если логин и пароль есть в локальном хранилище, то
    выполняем запрос на сервер, и в случае положтельного ответа и получения токена
    будем переходить на главный экран
 */

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel? = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel?.authorizationStatus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = com.onecab.core.R.drawable.logo_kilinka_2
                ),
                contentDescription = "logo",
                modifier = Modifier
                    .size(width = 216.dp, height = 150.dp)
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(70.dp),
                color = MaterialTheme.colorScheme.onBackground,
                strokeWidth = 7.dp
            )
        }

        Text(
            text = "Разработчик: ООО Автоматизация бизнеса",
            style = MaterialTheme.typography.labelMedium
                .copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        SplashScreen(viewModel = null)
    }
}