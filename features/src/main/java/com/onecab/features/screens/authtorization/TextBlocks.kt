package com.onecab.features.screens.authtorization

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val COMMON_WIDTH = 320

@Composable
fun TextBlock_20sp(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier
            .width(COMMON_WIDTH.dp)
            .padding(start = 15.dp, bottom = 15.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextBlock_16sp(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, bottom = 15.dp),
        textAlign = TextAlign.Center
    )
}
