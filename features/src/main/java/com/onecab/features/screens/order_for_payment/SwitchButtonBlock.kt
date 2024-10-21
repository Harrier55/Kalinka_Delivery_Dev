package com.onecab.features.screens.order_for_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
private fun SwitchButtonBlock(
    stateFeedback: Int,
    isError: Boolean,
    callbackFeedback: (feedback: Int) -> Unit,
    onClickToEmail: () -> Unit
) {
    val activeColor = MaterialTheme.colorScheme.tertiary
    val deActiveColor = Color.White
    val activeColorBorder = MaterialTheme.colorScheme.outline
    val errorColorBorder = MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 30.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    callbackFeedback.invoke(1)
                }
                .width(150.dp)
                .height(40.dp)
                .background(
                    color = if (stateFeedback == 1) {
                        activeColor
                    } else {
                        deActiveColor
                    },
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        errorColorBorder
                    } else {
                        activeColorBorder
                    },
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "По СМС",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
            )
        }

        Box(
            modifier = Modifier
                .clickable {
                    callbackFeedback.invoke(2)
                    onClickToEmail.invoke()
                }
                .width(150.dp)
                .height(40.dp)
                .background(
                    color = if (stateFeedback == 2) {
                        activeColor
                    } else {
                        deActiveColor
                    },
                    shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        errorColorBorder
                    } else {
                        activeColorBorder
                    },
                    shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "На эл.почту",
                style = MaterialTheme.typography.bodyLarge
                    .copy(color = Color.Black),
            )
        }
    }
}