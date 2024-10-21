package com.onecab.features.common_component.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.theme.KalinkaDeliveryTheme

@Composable
internal fun BlueButton(
    textButton: String,
    widthButton: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = {onClick.invoke()},
        enabled = enabled,
        modifier = modifier
            .height(50.dp)
            .width(widthButton.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = textButton,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewButton() {
    KalinkaDeliveryTheme {
        BlueButton(
            textButton = "TEXT BUTTON",
            widthButton = 260,
            enabled = true
        ){}
    }
}