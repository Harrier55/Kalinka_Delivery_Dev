package com.onecab.features.common_component.text_input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onecab.core.theme.KalinkaDeliveryTheme

@Composable
fun TextInputFieldWithShadow(
    spacerDown: Dp,
    width: Dp,
    placeholder: String,
    callback: (String) -> Unit,
) {
    val heightComponent = 40.dp // регулируем высоту компонента

    val message = remember { mutableStateOf("") }
    val isFocused = remember { mutableStateOf(false) }
    val shapes = MaterialTheme.shapes.large

    BasicTextField(
        value = message.value,
        onValueChange = { newText ->
            message.value = newText
            callback.invoke(newText)
        },
        modifier = Modifier
            .onFocusChanged {
                isFocused.value = it.isFocused
            }
            .padding(top = 0.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        cursorBrush = SolidColor(Color.Black),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .width(width)
                    .height(heightComponent)
                    .shadow(
                        elevation = 2.dp,
                        shape = shapes
                    )
                    .background(
                        Color.White,
                        shape = shapes
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = com.onecab.core.R.drawable.icon_search_1),
                    contentDescription = "search",
                    modifier = Modifier.padding(start = 8.dp).size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(width)
                ) {
                    if (message.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp)
                    }
                    innerTextField()
                }
            }
        }
    )
    Spacer(modifier = Modifier.size(spacerDown))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        TextInputFieldWithShadow(
            spacerDown = 10.dp,
            width = 320.dp,
            placeholder = "Введите название или адрес",
        ) {

        }
    }
}