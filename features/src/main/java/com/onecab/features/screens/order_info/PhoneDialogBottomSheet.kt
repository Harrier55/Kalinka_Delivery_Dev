package com.onecab.features.screens.order_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun PhoneDialogBottomSheet(
    openSheet: Boolean,
    closeSheet: () -> Unit,
    subTitle: String,
    phoneNumber: String,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit,
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
                subTitle = subTitle,
                phoneNumber = phoneNumber,
                onClickYes = {onClickYes.invoke()},
                onClickNo = {onClickNo.invoke()}
            )
        }
    }
}

@Composable
private fun ContentSheet(
    subTitle: String,
    phoneNumber: String,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit,
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
            text = "Вы уверены, что хотите позвонить",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
        )

        Row {
            Text(
                text = subTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                ),
            )

            Text(
                text = "?",
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        Text(
            text = "тел. $phoneNumber",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
        )

        Row {
            Button(
                onClick = { onClickNo.invoke() },
                modifier = Modifier
                    .padding(top = 60.dp)
                    .width(150.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "НЕТ")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = { onClickYes.invoke() },
                modifier = Modifier
                    .padding(top = 60.dp)
                    .width(150.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "ДА", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(
            subTitle = "ИП Пулькин",
            phoneNumber = "100500",
            onClickNo = {},
            onClickYes = {}
        )
    }
}