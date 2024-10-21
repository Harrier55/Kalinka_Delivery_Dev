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
import com.onecab.core.theme.KalinkaDeliveryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    textAppBar: String,
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier
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
                onClick = {onClickMenu.invoke()}
            ) {
                Icon(
                    painter = painterResource(id = com.onecab.core.R.drawable.icon_menu),
                    contentDescription = "menu",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        TopAppBarWithMenu(textAppBar = "Top app bar", onClickMenu = {})
    }
}