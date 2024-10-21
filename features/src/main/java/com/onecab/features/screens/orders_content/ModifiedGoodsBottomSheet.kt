package com.onecab.features.screens.orders_content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.GoodsEntity
import com.onecab.features.common_component.buttons.BlueButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifiedGoodsBottomSheet(
    openSheet: Boolean,
    closeSheet: () -> Unit,
    goodsEntity: GoodsEntity,
    callback: (amount: Double) -> Unit,
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
                goodsEntity = goodsEntity,
                onClickCallback = {
                    callback.invoke(it)
                }
            )
        }
    }
}


@Composable
private fun ContentSheet(
    goodsEntity: GoodsEntity,
    onClickCallback: (cout: Double) -> Unit
) {

    val textCount = if(goodsEntity.goodHasBeenModified){
        remember { mutableStateOf("${goodsEntity.modifyQuantity}") }
    }else{
        remember { mutableStateOf("${goodsEntity.quantity}") }
    }

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
            text = "${goodsEntity.commodityName}, ${goodsEntity.unitName}.",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        )

        Row(
            modifier = Modifier
                .imePadding()
                .padding(bottom = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.remove_24px),
                contentDescription = "minus",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable {
                        try {
                            val current = textCount.value
                                .replace(',', '.')
                                .toDouble()
                            textCount.value = String.format("%.3f", (current - 1))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            )

            TextField(
                value = textCount.value,
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center
                ),
                onValueChange = { newText ->
                    try {
                        if (newText.isNotEmpty()) {
                            // Получение последнего введенного символа
                            val lastChar = newText.last()

                            if (lastChar.isDigit() || lastChar == ',' || lastChar == '.') {
                                // Преобразование запятую в точки
                                val newValueWithComma = newText.replace(',', '.')
                                // подсчет количества точек
                                val commaCount = newValueWithComma.count { it == '.' }
                                // подсчет количества знаков после запятой
                                if (commaCount <= 1) {
                                    val parts = newValueWithComma.split('.')
                                    if (parts.size == 2 && parts[1].length <= 3) {
                                        textCount.value = newValueWithComma
                                    } else if (parts.size == 1) {
                                        textCount.value = newValueWithComma
                                    }
                                }
                            }
                        } else {
                            textCount.value = ""
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                modifier = Modifier
                    .width(150.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.DarkGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Icon(
                painter = painterResource(id = R.drawable.add_24px),
                contentDescription = "minus",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable {
                        try {
                            val current = textCount.value
                                .replace(',', '.')
                                .toDouble()
                            textCount.value = String.format("%.3f", (current + 1))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            )
        }

        BlueButton(
            textButton = "СОХРАНИТЬ",
            widthButton = 320,
            enabled = true,
            onClick = {
                try {
                    onClickCallback.invoke(textCount.value
                        .replace(',', '.')
                        .toDouble()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ContentSheet(goodsEntity = GoodsEntity()) {}
    }
}