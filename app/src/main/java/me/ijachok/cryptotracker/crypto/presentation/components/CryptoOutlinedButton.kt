package me.ijachok.cryptotracker.crypto.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CryptoOutlinedButton(
    modifier: Modifier = Modifier, text: String, onClick: () -> Unit, enabled: Boolean = true
) {

    val borderColor = if (enabled) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant

    val textColor = if (enabled) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurfaceVariant
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }

}

@PreviewLightDark
@Composable
private fun CryptoOutlinedButtonPreview() {
    CryptoTrackerTheme {
        CryptoOutlinedButton(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            text = "Button",
            onClick = {},
            enabled = true

        )
    }
}

@PreviewLightDark
@Composable
private fun CryptoOutlinedButtonPreviewDisabled() {
    CryptoTrackerTheme {
        CryptoOutlinedButton(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            text = "Button",
            onClick = {},
            enabled = false

        )
    }
}