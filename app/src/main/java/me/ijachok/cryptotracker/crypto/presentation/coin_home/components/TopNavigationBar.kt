package me.ijachok.cryptotracker.crypto.presentation.coin_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun TopNavigationBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Row(
        modifier
            .height(64.dp)
            .fillMaxWidth()
            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Spacer(Modifier.width(if (leadingIcon == null) 16.dp else 4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.weight(1f))
        if (trailingIcon != null) {
            trailingIcon()
        }
    }
}

@PreviewLightDark
@Composable
private fun LNBPrev1() {
    CryptoTrackerTheme {
        TopNavigationBar(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            title = "Top Nav Bar",
            leadingIcon = {},
            trailingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Rounded.Menu, contentDescription = null)
                }
            }

        )
    }
}
@PreviewLightDark
@Composable
private fun LNBPrev2() {
    CryptoTrackerTheme {
        TopNavigationBar(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            title = "Top Nav Bar",
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Rounded.Menu, contentDescription = null)
                }
            }

        )
    }
}