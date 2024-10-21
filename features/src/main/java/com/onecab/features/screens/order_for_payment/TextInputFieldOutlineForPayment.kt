package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.core.utils.SuffixTransformation


@Composable
internal fun TextInputFieldOutlineForPayment(
    width: Dp,
    placeholder: String,
    isError: Boolean,
    callback: (String) -> Unit,
) {

    val heightComponent = 40.dp // регулируем высоту компонента

    val message = remember { mutableStateOf("") }
    val isFocused = remember { mutableStateOf(false) }
    val focusColor = MaterialTheme.colorScheme.tertiary
    val focusDefault = MaterialTheme.colorScheme.outline
    val focusError = MaterialTheme.colorScheme.error

    println("--- placeholder = $placeholder")
    println("--- message… = ${message.value}")

    BasicTextField(
        value = message.value,
        onValueChange = { newText ->

            if(newText.isNotEmpty()){
                // Получение последнего введенного символа
                val lastChar = newText.last()

                if(lastChar.isDigit() || lastChar == ',' || lastChar == '.'){

                    // Преобразование точки в запятую
                    val newValueWithComma = newText.replace('.', ',')

                    val commaCount = newValueWithComma.count { it == ',' }

                    if (commaCount <= 1){
                        val parts = newValueWithComma.split(',')
                        if(parts.size == 2 && parts[1].length <= 2){
                            message.value = newValueWithComma
                        }else if (parts.size == 1){
                            message.value = newValueWithComma
                        }
                    }
                }
            }

            if(newText.isEmpty()) message.value = ""

//            if (newText.isDigitsOnly() || newText.contains("."))
//                message.value = newText
//
//            if (newText.isNotEmpty() &&
//                (newText.isDigitsOnly() || newText.contains("."))
//            )
//                callback.invoke(message.value)
//            else
//                callback.invoke("0")
        },
        modifier = Modifier
//            .onFocusChanged {
//                //    isFocused.value = it.isFocused
//            }
            .padding(top = 4.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        cursorBrush = SolidColor(Color.Black),
    //    singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = if (message.value.isNotEmpty()) SuffixTransformation(suffix = "  руб.")
        else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .width(width)
                    .height(heightComponent)
                    .background(Color.White, shape = MaterialTheme.shapes.small)
                    .border(
                        width = 1.dp,
                        color = if (isFocused.value) {
                            focusColor
                        } else if (isError) {
                            focusError
                        } else {
                            focusDefault
                        },
                        shape = MaterialTheme.shapes.small
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(width)
                ) {
                    if (message.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        TextInputFieldOutlineForPayment(
            width = 160.dp,
            placeholder = "100 rub",
            isError = false
        ) {

        }
    }
}
