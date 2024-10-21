package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.buttons.BlueButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputNewAmountSheet(
    openSheet: Boolean,
    closeSheet: () -> Unit,
    callback: (amount: String) -> Unit
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
                callback = {callback.invoke(it) }
            )
        }
    }
}


@Composable
private fun ContentSheet(
    callback: (amount: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
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
            text = "Ведите новое значение \n суммы заказа",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 40.dp)
        )

        val message = remember { mutableStateOf("") }

        BasicTextField(
            value = message.value,
            onValueChange = { newText ->

                if(newText.isNotEmpty()){
                    // Получение последнего введенного символа
                    val lastChar = newText.last()

                    if(lastChar.isDigit() || lastChar == ',' || lastChar == '.'){

                        // Преобразование точки в запятую
                        val newValueWithComma = newText.replace(',', '.')

                        val commaCount = newValueWithComma.count { it == '.' }

                        if (commaCount <= 1){
                            val parts = newValueWithComma.split('.')
                            if(parts.size == 2 && parts[1].length <= 2){
                                message.value = newValueWithComma
                            }else if (parts.size == 1){
                                message.value = newValueWithComma
                            }
                        }
                    }
                }

                if(newText.isEmpty()) message.value = ""
            },
            modifier = Modifier
                .onFocusChanged {/*Nothing*/}
                .padding(top = 4.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = SolidColor(Color.Black),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .width(300.dp)
                        .height(40.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .width(300.dp)
                    ) {
                        if (message.value.isEmpty()) {
                            Text(
                                text = "Внести сумму",
                                color = MaterialTheme.colorScheme.outline,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.size(40.dp))

        BlueButton(
            textButton = "ПОДТВЕРДИТЬ",
            widthButton = 300,
            enabled = true,
            onClick = { callback.invoke(message.value) }
        )
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet {}
    }
}