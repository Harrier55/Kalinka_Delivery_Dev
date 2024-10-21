package com.onecab.features.common_component.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onecab.core.R


@Composable
fun CombineAlertDialog(
    openDialog: Boolean,
    isLoading: Boolean,
    tickYes: Boolean,
    tickNo: Boolean,
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { /*Nothing*/ },
            confirmButton = { /*Nothing*/ },
            modifier = Modifier
                .size(200.dp),
            containerColor = Color.White,
            title = {
                Text(
                    text = if (tickYes) "Успешно!" else if (tickNo) "Ошибка" else "",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)
                                .fillMaxSize()
                                .padding(),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = 3.dp
                        )
                    }

                    if (tickYes) {
                        Image(
                            painter = painterResource(id = R.drawable.tick_yes),
                            contentDescription = "yes"
                        )
                    }

                    if (tickNo) {
                        Image(
                            painter = painterResource(id = R.drawable.tick_no),
                            contentDescription = "no"
                        )
                    }
                }
            }
        )
    }
}