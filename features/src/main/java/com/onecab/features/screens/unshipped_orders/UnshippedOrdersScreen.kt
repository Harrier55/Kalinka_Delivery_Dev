package com.onecab.features.screens.unshipped_orders

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.UnshippedOrdersEntity
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens

/**
 *      Заказы, которые были оплачены (получен положительный ответ об оплате от банка),
 *      но по каким-то причинам не были загружены на наш сервер
 */

private const val TAG = "UnshippedOrdersScreen"

@Composable
fun UnshippedOrdersScreen(
    modifier: Modifier = Modifier,
    viewModel: UnshippedOrdersViewModel? = hiltViewModel(),
) {
    Log.d(TAG, " +++ UnshippedOrdersScreen: start")

    val state = viewModel?.state?.collectAsState()?.value

    val defaultList = listOf(UnshippedOrdersEntity())
    val orderList = remember(state) {
        mutableStateOf(state?.unshippedOrdersEntityList ?: defaultList)
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.UnshippedOrdersScreen.title,
                onClickArrowBack = { viewModel?.clickArrowBack() }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding())
                .width(320.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orderList.value) { item: UnshippedOrdersEntity ->
                    ItemOrder(order = item)
                }
            }
        }
    }
}

@Composable
private fun ItemOrder(order: UnshippedOrdersEntity) {
    Column(
        modifier = Modifier
            .width(320.dp)
            .wrapContentHeight()
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.small
            )
            .background(color = Color.White, shape = MaterialTheme.shapes.small)
    ) {
        Text(
            text = "Заказ № ${order.orderNumber}",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = order.purchaserName,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        )

        Text(
            text = "Сумма оплаты: ${order.paymentAmount}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        )

        Text(
            text = "Дата оплаты: ${order.paymentDate}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "Код ответа СБП: ${order.pay_api_info}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )

        RowBlock(text = "Оплата по СБП", isDone = order.doneSbp)
        RowBlock(text = "Не загружен, как оплаченный", isDone = order.doneAddPay)
        RowBlock(text = "Не загружен, как доставленный", isDone = order.doneAddDelivery)
    }
}

@Composable
private fun RowBlock(
    text: String,
    isDone: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "check",
            tint = if (isDone)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.error
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        UnshippedOrdersScreen(
            viewModel = null
        )
    }
}