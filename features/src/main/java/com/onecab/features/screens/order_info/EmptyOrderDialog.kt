package com.onecab.features.screens.order_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.onecab.features.common_component.app_bar.TopAppBarWithArrowBack
import com.onecab.features.common_component.screenModifier_1
import com.onecab.features.navigation_route.Screens


@Composable
internal fun EmptyOrderDialog(
    show: Boolean,
    closeDialog: () -> Unit
) {
    if(show){

        Dialog(
            onDismissRequest = { /*Nothing*/ },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                TopAppBarWithArrowBack(
                    textAppBar = Screens.OrdersInfoScreen.title,
                    onClickArrowBack = {closeDialog.invoke()}
                )

                Column(
                    modifier = Modifier
                        .screenModifier_1(padding = 0.dp)
                        .width(320.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 30.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )

                    Text(
                        text = "Заказ не найден",
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}
