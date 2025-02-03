package me.ijachok.cryptotracker.crypto.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties()
    ) {
        Surface(
            modifier,
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = displayFontFamily,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.align(Alignment.End)) {
                    TextButton(
                        modifier = Modifier,
                        onClick = onCancel
                    ) {
                        Text(
                            text = cancelButtonText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    CryptoOutlinedButton(
                        modifier = Modifier,
                        text = confirmButtonText,
                        onClick = onConfirm,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SDPrev() {
    CryptoTrackerTheme {
        SimpleDialog(
            modifier = Modifier.fillMaxWidth(),
            title = "Dialog title",
            subtitle = "Dialog subtitle",
            confirmButtonText = "Confirm",
            cancelButtonText = "Cancel",
            onConfirm = {},
            onCancel = {}
        )
    }
}