package com.onecab.features.screens.report

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.LoadIndicator
import com.onecab.features.common_component.app_bar.TopAppBarWithMenu
import com.onecab.features.common_component.buttons.SearchDateButton
import com.onecab.features.common_component.dialogs.DatePickerCustomDialog
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens
import kotlinx.coroutines.launch

private const val TAG = "ReportScreen"
private const val COMMON_WIDTH = 320

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportScreenViewModel? = hiltViewModel()
) {
    Log.i(TAG, "+++ ReportScreen start")

    val coroutineScope = rememberCoroutineScope()
    val state = viewModel?.state?.collectAsState()?.value

    val currentDate = remember(state) {
        mutableStateOf(state?.currentDateForButton ?: "11.11.1111")
    }
    val countOrders = remember(state) {
        mutableStateOf(state?.countOrders ?: "1")
    }
    val paymentAmount = remember(state) {
        mutableStateOf(state?.paymentAmount ?: "2")
    }
    val paymentOrders = remember(state) {
        mutableStateOf(state?.paymentOrders ?: "3")
    }
    val unsentPayments = remember(state) {
        mutableStateOf(state?.unsentPayments ?: "4")
    }
    val isLoading = remember(state) {
        mutableStateOf(state?.isLoading ?: false)
    }
    val showDateSelectDialog = remember(state) {
        mutableStateOf(state?.showDateSelectDialog ?: false)
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithMenu(
                textAppBar = Screens.ReportScreen.title,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            SearchDateButton(
                text = currentDate.value,
                spaceDown = 10.dp,
                onClick = { viewModel?.openDateSelectDialog() }
            )

            LoadIndicator(isLoading = isLoading.value)

            TextBlockWithValue(text = "Все заказы", value = countOrders.value)

            TextBlockWithValue(text = "Сумма по СБП [руб]", value = "${paymentAmount.value} руб.")

            TextBlockWithButton(
                text = "Выполненные заказы",
                value = paymentOrders.value,
                onClick = {viewModel?.navToCompleteOrders()}
            )

            TextBlockWithButton(
                text = "Неотправленные заказы",
                value = unsentPayments.value,
                onClick = {viewModel?.navToUnshippedOrders()}
            )
        }

        // Диалог выбора даты
        DatePickerCustomDialog(
            showDialog = showDateSelectDialog.value,
            dismiss = { viewModel?.closeDateSelectDialog() },
            callbackDate = {
                viewModel?.onClickButtonSelectDate(selectDate = it)
            }
        )
    }
}

@Composable
private fun TextBlockWithValue(
    text: String,
    value: String
) {
    Row(
        modifier = Modifier
            .width(COMMON_WIDTH.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black
            )
        )
    }
}

@Composable
private fun TextBlockWithButton(
    text: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(COMMON_WIDTH.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )

        Column(
            modifier = Modifier
                .width(50.dp)
                .height(40.dp)
                .clickable { onClick.invoke() }
                .shadow(
                    elevation = 2.dp,
                    shape = MaterialTheme.shapes.small
                )
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.small
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ReportScreen(viewModel = null)
    }
}