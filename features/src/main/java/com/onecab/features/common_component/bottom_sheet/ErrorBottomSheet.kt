package com.onecab.features.common_component.bottom_sheet

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
fun ErrorBottomSheet(
    openSheet: Boolean,
    closeSheet: () -> Unit,
    title: String,
    textError: String,
    modifier: Modifier = Modifier
) {

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (openSheet) {
        ModalBottomSheet(
            onDismissRequest = { closeSheet.invoke() },
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
                            .clickable { closeSheet.invoke() }
                    )
                    Box(modifier = modifier.size(16.dp))
                }
            }
        ) {
            ContentSheet(
                title = title,
                textError = textError
            )
        }
    }
}

@Composable
private fun ContentSheet(
    title: String,
    textError: String
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
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
        )

        Text(
            text = textError,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(
            title = "Произошла ошибка при обращении к серверу",
            textError = "error server")
    }
}