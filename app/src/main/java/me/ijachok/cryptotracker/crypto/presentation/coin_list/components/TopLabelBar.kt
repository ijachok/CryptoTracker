package me.ijachok.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun TopLabelBar(modifier: Modifier = Modifier, iconButton: @Composable () -> Unit) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(R.mipmap.ic_app_arrows),
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.weight(1f))
        iconButton()
    }
}

@PreviewLightDark
@Composable
private fun LTBPrev() {
    CryptoTrackerTheme {
        TopLabelBar(Modifier.background(MaterialTheme.colorScheme.surface)) {
            IconButton(
                modifier = Modifier,
                onClick = {},

                ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}