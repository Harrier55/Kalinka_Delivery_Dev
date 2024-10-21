package com.onecab.features.screens.complete_orders_info

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens

private const val COMMON_WIDTH = 320
private const val TAG = "CompleteOrderInfoScreen"

@Composable
fun CompleteOrdersInfoScreen(
    orderId: String,
    modifier: Modifier = Modifier,
    viewModel: CompleteOrderInfoViewModel? = hiltViewModel()
) {
    Log.i(TAG, " +++ CompleteOrderInfoScreen: start____OrderId = $orderId")

    val state = viewModel?.state?.collectAsState()?.value

    val orderNumber= remember(state) {
        mutableStateOf(state?.orderEntity?.orderNumber?:"12345")
    }
    val date = remember(state) {
        mutableStateOf(state?.orderEntity?.deliveryDate?:"15.15.1515")
    }
    val address = remember(state) {
        mutableStateOf(state?.orderEntity?.deliveryAddress?:"Street")
    }
    val client = remember(state) {
        mutableStateOf(state?.orderEntity?.purchaserName?:"Smeeth")
    }
    val debt = remember(state) {
        mutableStateOf(state?.debt?: "---")
    }
    val consist = remember(state) {
        mutableStateOf(state?.consist?: "---")
    }
    val weight = remember(state) {
        mutableStateOf(state?.weight?: "---")
    }
    val comment = remember(state) {
        mutableStateOf(state?.orderEntity?.comment?:"comment")
    }
    val amount = remember(state) {
        mutableStateOf(state?.orderEntity?.paymentAmount?:"15000")
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen(orderId = orderId)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.CompleteOrdersInfoScreen.title + orderNumber.value,
                onClickArrowBack = { viewModel?.onClickArrowBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding())
                .width(COMMON_WIDTH.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "завершен ${date.value}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(COMMON_WIDTH.dp),
                textAlign = TextAlign.End
            )

            TextBlockWithIconButton(
                textValue = "Адрес: ${address.value}",
                iconId = R.drawable.icon_address,
                onClickIcon = {
                    //todo
                }
            )

            TextBlockWithIconButton(
                textValue = "Клиент: ${client.value}",
                iconId = R.drawable.icon_phone,
                onClickIcon = {
//                    showCallPhoneDialog.value = true
                }
            )

            TextBlockWithIconButton(
                textValue = "Задолженность: ${debt.value} руб.",
                iconId = R.drawable.icon_debt,
                onClickIcon = {
//                    viewModel?.navToDebtScreen(orderId = orderId)
                }
            )

            TextBlockWithIconButton(
                textValue = "Состав: ${consist.value} позиции, ${weight.value} кг.",
                iconId = R.drawable.icon_consist,
                onClickIcon = {
                    viewModel?.navToOrdersContent(orderId = orderId)
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
                    .padding(top = 8.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .width(COMMON_WIDTH.dp)
            ) {
                Text(
                    text = "Комментарий: ",
                    style = MaterialTheme.typography.bodySmall
                        .copy(color = MaterialTheme.colorScheme.surface),
                )
                Text(
                    text = comment.value,
                    style = MaterialTheme.typography.labelLarge
                        .copy(color = MaterialTheme.colorScheme.surface),
                )
            }

            Text(
                text = "Оплачено ${amount.value} руб.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .width(COMMON_WIDTH.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TextBlockWithIconButton(
    textValue: String,
    iconId: Int,
    onClickIcon: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .width(COMMON_WIDTH.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = textValue,
            style = MaterialTheme.typography.labelLarge
                .copy(color = Color.Black),
            modifier = Modifier.weight(0.5f)
        )

        Card(
            onClick = { onClickIcon.invoke() },
            modifier = Modifier
                .width(50.dp)
                .height(40.dp),
            shape = ShapeDefaults.Small,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "address",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        CompleteOrdersInfoScreen(orderId = "123", viewModel = null)
    }
}