package com.onecab.features.common_component.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.features.common_component.buttons.BlueButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCustomDialog(
    showDialog: Boolean,
    dismiss: () -> Unit,
    callbackDate: (selectDate: Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { dismiss.invoke() },
            confirmButton = {
                BlueButton(
                    textButton = "Выбрать",
                    widthButton = 120,
                    enabled = true,
                    onClick = {
                        callbackDate.invoke(datePickerState.selectedDateMillis)
                    }
                )
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = {},
                headline = {
                    Text(
                        text = "Выбрать дату",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    todayDateBorderColor = Color.Red,
                    selectedDayContentColor = Color.Cyan,
                    selectedDayContainerColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        DatePickerCustomDialog(
            showDialog = true,
            dismiss = {},
            callbackDate = {}
        )
    }
}