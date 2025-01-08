package me.ijachok.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.crypto.domain.DisplayableNumber
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun PriceChange(
    modifier: Modifier = Modifier,
    change: DisplayableNumber,
    fontSize:TextUnit = 14.sp,
    showArrow:Boolean = true
) {
    val contentColor = if(change.value < 0.0) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    val backgroundColor = if(change.value < 0.0) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val text = if(!showArrow && change.value > 0.0){
        "+${change.formatted}"
    } else if(showArrow && change.value < 0.0){
        change.formatted.drop(1)
    }else change.formatted

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .background(backgroundColor)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(showArrow){
            Icon(
                imageVector = if (change.value < 0.0) {
                    Icons.Rounded.KeyboardArrowDown
                } else {
                    Icons.Rounded.KeyboardArrowUp
                },
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )
        }
        Text(
            text = "${text}%",
            color = contentColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

@PreviewLightDark
@Composable
private fun PriceChangePreview() {
    CryptoTrackerTheme {
        PriceChange(
            change = DisplayableNumber(
                value = 2.43,
                formatted = "2.43"
            )
        )
    }
}
@PreviewLightDark
@Composable
private fun PriceChangeDownPreview() {
    CryptoTrackerTheme {
        PriceChange(
            change = DisplayableNumber(
                value = -2.43,
                formatted = "-2.43"
            )
        )
    }
}