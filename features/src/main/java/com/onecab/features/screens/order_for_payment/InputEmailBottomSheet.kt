package com.onecab.features.screens.order_for_payment

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.buttons.BlueButton
import com.onecab.features.common_component.text_input.TextInputFieldOutline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEmailBottomSheet(
    openSheet: Boolean,
    isValidEmail: Boolean,
    closeSheet: () -> Unit,
    callback: (email: String) -> Unit
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
                        painter = painterResource(id = com.onecab.core.R.drawable.icon_close),
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
                isValidEmail = isValidEmail,
                callback = {callback.invoke(it) }
            )
        }
    }
}

@Composable
private fun ContentSheet(
    isValidEmail: Boolean,
    callback: (email: String) -> Unit
) {

    val inputText = remember { mutableStateOf("") }

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
            text = "Ведите адрес электронной \n почты",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 40.dp)
        )

        TextInputFieldOutline(
            spacerDown = 4.dp,
            width = 300.dp,
            placeholder = "example@mail.com",
            isError = false,
            callback = { inputText.value = it }
        )
        Box(modifier = Modifier.height(18.dp)) {
            if (isValidEmail){
                Text(
                    text = "Неверно указан адрес электронной почты",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        BlueButton(
            textButton = "ДАЛЕЕ",
            widthButton = 300,
            enabled = true,
            onClick = { callback.invoke(inputText.value) }
        )
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(isValidEmail = true) {}
    }
}