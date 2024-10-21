package com.onecab.features.common_component.top_app_bar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopAppBarClear(
    textAppBar: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = textAppBar,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxSize()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        windowInsets = WindowInsets(top = 40.dp),
        modifier = Modifier
            .height(80.dp),
        )
}

@Preview(showBackground = true)
@Composable
private fun PreviewBar() {
    KalinkaDeliveryTheme {
        TopAppBarClear(textAppBar = "Text App Bar")
    }
}

