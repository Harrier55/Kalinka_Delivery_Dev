package com.onecab.features.common_component.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme

@Composable
fun SearchDateButton(
    text: String,
    spaceDown: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {onClick.invoke()},
    ) {
        Row {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "calendar",
            tint = Color.Black
        )
    }
    Spacer(modifier = modifier.height(spaceDown))
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        SearchDateButton(
            text = "11.11.2011",
            spaceDown = 10.dp,
            onClick = {}
        )
    }
}