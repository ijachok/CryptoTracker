package me.ijachok.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun CoinListItemMini(modifier: Modifier = Modifier, coinUi: CoinUi, onClick: () -> Unit) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            imageVector = ImageVector.vectorResource(coinUi.iconRes),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = coinUi.name
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = coinUi.symbol,
            fontSize = 14.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "$ ${coinUi.priceUsd.formatted}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = displayFontFamily,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@PreviewLightDark
@Composable
private fun CoinListMiniPrev() {
    CryptoTrackerTheme {
        Column(
            Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (0..3).forEach {
                CoinListItemMini(Modifier, previewCoinUi) {}
            }
        }
    }
}