package com.onecab.features.screens.order_list_main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.OrderEntity
import com.onecab.features.common_component.LoadIndicator
import com.onecab.features.common_component.app_bar.TopAppBarWithMenuAndAction
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.common_component.text_input.TextInputFieldWithShadow
import com.onecab.features.navigation_route.Screens
import kotlinx.coroutines.launch

/*
В экране есть логика загрузки- автоматическая загрузка с сервера происходит только
при первом открытии экрана. Дальнейшие обновления происходят только по скайпу вниз.
Иначе данные будут приходить только из кэша
 */

const val TAG = "OrdersListScreen"

@Composable
fun OrdersListScreen(
    modifier: Modifier = Modifier,
    viewModel: OrdersListScreenViewModel? = hiltViewModel(),
) {
    Log.d(TAG, "+++ OrdersListScreen: start")

    val coroutineScope = rememberCoroutineScope()

    val state = viewModel?.state?.collectAsState()?.value
    val defaultFakeList = listOf(OrderEntity(), OrderEntity())
    val ordersList = remember(state) {
        mutableStateOf(state?.listOrders ?: defaultFakeList)
    }
    val isLoading = remember(state) {
        mutableStateOf(state?.isLoading ?: false)
    }
    val showUpdateBoxIndicator = remember(state) {
        mutableStateOf(state?.showUpdateBlock?: false)
    }

    // эти две переменные нужны для отслеживания свайпа вниз
    val lazyScrollState = rememberLazyListState()
    val swipeDown = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel?.fillFirstScreen()
    }

    LaunchedEffect(key1 = swipeDown.value) {
        if (lazyScrollState.isScrollInProgress &&
            lazyScrollState.firstVisibleItemIndex == 0 &&
            lazyScrollState.firstVisibleItemScrollOffset == 0 &&
            swipeDown.value
        ) {
            Log.i(TAG, "--- swipe down ")
            viewModel?.fillBySwipeScreen()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBarWithMenuAndAction(
                textAppBar = Screens.OrdersListScreen.title,
                menuIconClick = {
                    coroutineScope.launch {
                        viewModel?.openNavigationDrawer()
                    }
                },
                actionIconClick = {
                    viewModel?.navToScan()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .screenModifier_1(padding = padding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            TextInputFieldWithShadow(
                spacerDown = 20.dp,
                width = 320.dp,
                placeholder = "Введите название или адрес",
                callback = { viewModel?.searchFunction(text = it) }
            )

            LoaderBoxIndicator(isLoad = isLoading.value)

            UpdateBoxIndicator(
                visible = showUpdateBoxIndicator.value,
                onClick = {viewModel?.loadFromServer()}
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .nestedScroll(object : NestedScrollConnection {
                        override fun onPostScroll(  // отслеживание свайпа вниз
                            consumed: Offset,
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            if (available.y > 0) {
                                swipeDown.value = true
                            } else {
                                swipeDown.value = false
                            }
                            return super.onPostScroll(consumed, available, source)
                        }
                    }),
                state = lazyScrollState,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 20.dp, bottom = 30.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(ordersList.value) { listItem ->
                    OrderItem(
                        orderEntity = listItem,
                        onClick = {
                            viewModel?.onClickOrderItem(orderId = listItem.orderId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UpdateBoxIndicator(
    visible: Boolean,
    onClick: () -> Unit
) {
    if (visible){
        Spacer(modifier = Modifier.height(50.dp))
        Box(modifier = Modifier
            .clickable { onClick.invoke() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Список заказов пуст",
                    color = MaterialTheme.colorScheme.outline
                )
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(80.dp)
                )
            }
        }
    }
}

@Composable
private fun LoaderBoxIndicator(
    isLoad: Boolean
) {
    if(isLoad){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadIndicator(isLoading = isLoad)
            Text(text = "Обновление списка заказов", fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview44() {
    KalinkaDeliveryTheme {
        OrdersListScreen(viewModel = null)
    }
}