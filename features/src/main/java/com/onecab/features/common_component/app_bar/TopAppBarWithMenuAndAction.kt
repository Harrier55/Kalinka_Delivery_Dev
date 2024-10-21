package com.onecab.features.common_component.app_bar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenuAndAction(
    textAppBar: String,
    modifier: Modifier = Modifier,
    menuIconClick: () -> Unit,
    actionIconClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = textAppBar,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        windowInsets = WindowInsets(top = 20.dp),
        modifier = Modifier
            .height(80.dp),
        navigationIcon = {
            IconButton(
                onClick = {menuIconClick.invoke()}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_menu),
                    contentDescription = "setting",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    actionIconClick.invoke()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.qr_code_icon),
                    contentDescription = "qr code",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

    )
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        TopAppBarWithMenuAndAction(
            textAppBar = "TopAppBarWithMenuAndAction",
            menuIconClick = { /*TODO*/ }) {

        }
    }
}