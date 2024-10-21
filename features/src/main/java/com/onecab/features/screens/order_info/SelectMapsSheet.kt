package com.onecab.features.screens.order_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMapsSheet(
    openSheet: Boolean,
    address: String,
    dismiss: () -> Unit,
    onClickItem: (item: Int) -> Unit
) {

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (openSheet) {
        ModalBottomSheet(
            onDismissRequest = { dismiss.invoke() },
            sheetState = modalBottomSheetState,
            containerColor = Color.Transparent,
            dragHandle = {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_close),
                        contentDescription = "close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { dismiss.invoke() }
                    )
                    Box(modifier = Modifier.size(16.dp))
                }
            }
        ) {
            ContentSheet(
                address = address,
                onClickItem = { onClickItem.invoke(it) }
            )
        }
    }
}

@Composable
fun ContentSheet(
    address: String,
    onClickItem: (item: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Показать адрес:",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp, bottom = 24.dp)
        )

        Text(
            text = address,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val listItems =
            listOf("В Google Maps", "В приложении", "В другом браузере")

        listItems.forEachIndexed { index, it ->
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { onClickItem.invoke(index) }
            )
            HorizontalDivider()
        }


    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(address = "Moskow") {}
    }
}