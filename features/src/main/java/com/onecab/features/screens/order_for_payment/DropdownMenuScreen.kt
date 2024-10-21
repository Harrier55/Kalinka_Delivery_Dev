package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme

@Composable
fun DropdownMenuScreen4(
    isError: Boolean,
    callbackSelectedItem: (selectedItem: String) -> Unit
) {

    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }

    val items = listOf("Причина 1", "Причина 2", "Причина 3")

    val isFocused = remember { mutableStateOf(false) }
    val focusColor = MaterialTheme.colorScheme.tertiary
    val focusDefault = MaterialTheme.colorScheme.outline
    val focusError = Color.Red

    Column(
        modifier = Modifier
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded.value = !expanded.value }
                .width(300.dp)
                .height(40.dp)
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if(selectedItem.value.isEmpty()) "Выбрать причину коррекции" else selectedItem.value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 15.dp)
            )

            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .width(320.dp)
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        selectedItem.value = item
                        expanded.value = false
                        callbackSelectedItem.invoke(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    KalinkaDeliveryTheme {
        DropdownMenuScreen4(
            isError = false,
            callbackSelectedItem = {}
        )
    }
}