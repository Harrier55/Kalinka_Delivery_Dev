package com.onecab.features.screens.complete_orders_content

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.GoodsEntity
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens

private const val COMMON_WIDTH = 320
private const val TAG = "CompleteOrdersContentScreen"

@Composable
fun CompleteOrdersContentScreen(
    orderId: String,
    modifier: Modifier = Modifier,
    viewModel: CompleteOrdersContentViewModel? = hiltViewModel()
) {
    Log.i(TAG, "+++ CompleteOrdersContentScreen: orderId = $orderId")

    val state = viewModel?.stateForContent?.collectAsState()?.value

    val quantity = remember(state) {
        mutableStateOf(state?.quantity ?: "15")
    }
    val unit = remember(state) {
        mutableStateOf(state?.unit ?: "кг")
    }
    val count = remember(state) {
        mutableStateOf(state?.count ?: "40")
    }
    val countName = remember(state) {
        mutableStateOf(state?.countName ?: "позиц")
    }
    val defaultList = listOf(GoodsEntity(), GoodsEntity())
    val listGoods = remember(state) {
        mutableStateOf(state?.listGoods ?: defaultList)
    }

    LaunchedEffect(key1 = true) {
        viewModel?.fetchContentScreen(orderId = orderId)
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithArrowBack(
                textAppBar = Screens.CompleteOrdersContentScreen.title,
                onClickArrowBack = { viewModel?.onClickArrowBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextRow(
                quantity = quantity.value,
                unit = unit.value,
                count = count.value,
                countName = countName.value
            )

            LazyColumn(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .width(COMMON_WIDTH.dp)
                    .weight(1f)
            ) {
                items(listGoods.value) { goodsEntity ->
                    Item(
                        goodsEntity = goodsEntity,
                    )
                    HorizontalDivider()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

        }
    }
}


@Composable
private fun Item(
    goodsEntity: GoodsEntity,
) {
    Row(
        modifier = Modifier
            .width(320.dp)
            .wrapContentHeight()
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (goodsEntity.goodHasBeenModified) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_edit_pen),
                    contentDescription = "edit",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(18.dp)
                )
            }
        }

        Text(
            text = goodsEntity.commodityName,
            style = MaterialTheme.typography.labelLarge
                .copy(color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )

        if (goodsEntity.goodHasBeenModified) {
            Column {
                Text(
                    text = "${goodsEntity.modifyQuantity} ${goodsEntity.unitName}.",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(color = Color.Black),
                )
                Text(
                    text = "было ${goodsEntity.quantity}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

        } else {
            Text(
                text = "${goodsEntity.quantity} ${goodsEntity.unitName}.",
                style = MaterialTheme.typography.bodyMedium
                    .copy(color = Color.Black),
            )
        }
    }
}

@Composable
private fun TextRow(
    quantity: String,
    unit: String,
    count: String,
    countName: String
) {
    Row(
        modifier = Modifier
            .padding(top = 30.dp)
            .width(COMMON_WIDTH.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$quantity $unit",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "$count $countName",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        CompleteOrdersContentScreen(viewModel = null, orderId = "")
    }
}