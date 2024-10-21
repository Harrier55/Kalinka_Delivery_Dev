package com.onecab.features.screens.order_debt


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.DebtEntity
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens

private const val COMMON_WIDTH = 320
private const val TAG = "OrderDebtScreen"

@Composable
fun OrderDebtScreen(
    orderId: String,
    modifier: Modifier = Modifier,
    viewModel: OrderDebtScreenViewModel? = hiltViewModel(),
) {
    Log.i(TAG, "OrderDebtScreen: with orderId = $orderId")

    val state = viewModel?.state?.collectAsState()?.value

    //values
    val commonDebt = remember(state) {
        mutableStateOf(state?.commonDebt ?: "125")
    }
    val debtListDefault = listOf(
        DebtEntity(
            accountDocumentNumber = "RE1255884",
            amount = 458.0,
            status = "no debt"
        )
    )
    val debtList = remember(state) {
        mutableStateOf(state?.debtList ?: debtListDefault)
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchScreen()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.OrdersDebtScreen.title,
                onClickArrowBack = { viewModel?.clickArrowBack() }
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
                text = "${commonDebt.value} руб.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(COMMON_WIDTH.dp),
                textAlign = TextAlign.End
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                items(debtList.value) { orderDebtEntity ->
                    Item(debtEntity = orderDebtEntity)

                    HorizontalDivider(
                        modifier = Modifier
                            .width(COMMON_WIDTH.dp)
                            .padding(top = 8.dp, bottom = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    debtEntity: DebtEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(top = 16.dp)
            .width(COMMON_WIDTH.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "№ Документа",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            Text(
                text = debtEntity.accountDocumentNumber,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = "${debtEntity.amount} руб."
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        OrderDebtScreen(
            orderId = "",
            viewModel = null
        )
    }
}