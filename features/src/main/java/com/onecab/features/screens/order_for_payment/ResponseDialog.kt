package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onecab.core.R


@Composable
fun ResponseDialog(
    showDialog: Boolean,
    isLoading: Boolean,
    payStatus: Boolean,
    title: String,
    dismiss: () -> Unit,
    onClickButton: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /*Nothing*/ },
            confirmButton = {
                if (!isLoading) {
                    if (payStatus) {
                        Button(onClick = { onClickButton.invoke() }) {
                            Text(
                                text = "ОТМЕТИТЬ ЗАКАЗ, КАК ДОСТАВЛЕННЫЙ",
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        Button(onClick = { dismiss.invoke() }) {
                            Text(
                                text = "ЗАКРЫТЬ",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            title = {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(90.dp)
                                .fillMaxSize()
                                .padding(),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = 3.dp
                        )
                    } else {
                        if (payStatus) {
                            Image(
                                painter = painterResource(id = R.drawable.tick_yes),
                                contentDescription = "yes",
                                modifier = Modifier.size(150.dp),
                            )
                        }

                        if (!payStatus) {
                            Image(
                                painter = painterResource(id = R.drawable.tick_no),
                                contentDescription = "no",
                                modifier = Modifier.size(150.dp),
                            )
                        }
                    }
                }
            }
        )
    }
}