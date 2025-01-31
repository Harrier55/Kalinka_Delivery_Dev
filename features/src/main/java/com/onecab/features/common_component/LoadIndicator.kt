package com.onecab.features.common_component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme

@Composable
fun LoadIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading){
        CircularProgressIndicator(
            modifier = modifier
                .size(40.dp)
                .fillMaxSize()
                .padding(),
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = 3.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        LoadIndicator(isLoading = true)
    }
}