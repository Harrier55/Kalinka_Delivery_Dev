package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.buttons.BlueButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeBottomSheet(
    openSheet: Boolean,
    title: String,
    closeSheet: () -> Unit,
    image: ImageBitmap?,
    paymentAmount: String,
    correctAmount: String,
    timerCount: String,
    showTimer: Boolean,
    checkPayment: () -> Unit
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
                    Box(modifier = Modifier.size(16.dp))
                }
            }
        ) {
            ContentSheet(
                title = title,
                paymentAmount = paymentAmount,
                correctAmount = correctAmount,
                image = image,
                timerCount = timerCount,
                showTimer = showTimer,
                checkPayment = { checkPayment.invoke() }
            )
        }
    }
}

@Composable
private fun ContentSheet(
    title: String,
    paymentAmount: String,
    correctAmount: String,
    timerCount: String,
    showTimer: Boolean,
    image: ImageBitmap?,
    checkPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
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
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        )

        if (correctAmount.isEmpty()) {
            Text(
                text = "Сумма оплаты $paymentAmount руб.",
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            Text(
                text = "Сумма оплаты $correctAmount руб.",
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Image(
            bitmap = image!!,
            contentDescription = ""
        )

        if (showTimer) {
            Text(
                text = "Время действия кода: $timerCount",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showTimer) {
            BlueButton(
                textButton = "ПРОВЕРИТЬ ОПЛАТУ",
                widthButton = 300,
                enabled = true,
                onClick = { checkPayment.invoke() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(
            title = "Покажите QR-код клиенту",
            paymentAmount = "321",
            image = null,
            showTimer = true,
            timerCount = "11-22",
            correctAmount = "100500",
            checkPayment = {}
        )
    }
}