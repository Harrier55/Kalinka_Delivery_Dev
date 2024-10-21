package com.onecab.features.screens.complete_orders

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.OrderEntity
import com.onecab.features.common_component.LoadIndicator
import com.onecab.features.common_component.app_bar.TopAppBarWithMenu
import com.onecab.features.common_component.buttons.SearchDateButton
import com.onecab.features.common_component.dialogs.DatePickerCustomDialog
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.common_component.text_input.TextInputFieldWithShadow
import com.onecab.features.navigation_route.Screens
import kotlinx.coroutines.launch

/**
 * Функция отображает завершенные заказы.
 *
 * Здесь есть внутренняя логига:
 * Дата запроса может быть разная. Если на эеран переходим из гравного меню - дата сегодняшняя.
 * Но есть еще переход из экрана Сводка. дата может прийти оттуда, тогда запрос выполняем по ней.
 *
 *  Проверим, есть ли в списке приходящих с сервера заказов такие, в которых
 *  были изменения товаров, если таковые есть - пометить их иконкой
 *
 *  На сегодня отображаются товары с фильтром status.isNotEmpty()
 *  а на любой другой день - без фильтра, т.е все заказы
 */

private const val TAG = "CompletedOrders"

@Composable
fun CompletedOrdersScreen(
    dateRequest: String,
    modifier: Modifier = Modifier,
    viewModel: CompletedOrdersViewModel? = hiltViewModel()
) {
    Log.d(TAG, "+++ CompletedOrders start__dateRequest $dateRequest")

    val coroutineScope = rememberCoroutineScope()

    val state = viewModel?.state?.collectAsState()?.value
    val defaultOrdersList = listOf(OrderEntity(), OrderEntity())
    val currentDate = remember(state) {
        mutableStateOf(state?.currentDate ?: "12.12.1212")
    }
    val ordersList = remember(state) {
        mutableStateOf(state?.ordersList ?: defaultOrdersList)
    }
    val isLoading = remember(state) {
        mutableStateOf(state?.isLoading ?: false)
    }
    val showDateSelectDateDialog = remember(state) {
        mutableStateOf(state?.showSelectDateDialog ?: false)
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fillScreen(dateRequest = dateRequest)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithMenu(
                textAppBar = Screens.CompletedOrdersScreen.title,
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
            SearchDateButton(
                text = currentDate.value,
                spaceDown = 16.dp,
                onClick = { viewModel?.showSelectDateDialog() }
            )

            TextInputFieldWithShadow(
                spacerDown = 20.dp,
                width = 320.dp,
                placeholder = "Введите название или адрес",
                callback = { text -> viewModel?.searchFunction(text = text) }
            )

            LoadIndicator(isLoading = isLoading.value)

            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(ordersList.value) { item: OrderEntity ->
                    OrderItemComplete(
                        orderEntity = item,
                        onClick = { orderId ->
                            viewModel?.onClickOrderItem(orderId = orderId)
                        }
                    )
                }
            }
        }

        // Диалог выбора даты
        DatePickerCustomDialog(
            showDialog = showDateSelectDateDialog.value,
            dismiss = { viewModel?.closeSelectDateDialog() },
            callbackDate = {
                viewModel?.onClickSelectedDate(selectDate = it)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewHistoryList() {
    KalinkaDeliveryTheme {
        CompletedOrdersScreen(
            dateRequest = "",
            viewModel = null
        )
    }
}