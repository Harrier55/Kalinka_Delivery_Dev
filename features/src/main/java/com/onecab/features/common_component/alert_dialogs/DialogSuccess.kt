package com.onecab.features.common_component.alert_dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.onecab.core.R

@Composable
internal fun DialogSuccess(
    openDialog: Boolean,
    textDialog: String,
    onCloseDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(openDialog){
        Dialog(
            onDismissRequest = { onCloseDialog.invoke() },
            content = {
                Column(
                    modifier = modifier
                        .background(
                            color = Color.White,
                            shape = MaterialTheme.shapes.large
                        )
                        .width(260.dp)
                        .height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = textDialog,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 35.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.icon_success),
                        contentDescription = "success",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        )
    }
}