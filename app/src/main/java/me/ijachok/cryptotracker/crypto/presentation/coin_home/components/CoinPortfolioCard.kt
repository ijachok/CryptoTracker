package me.ijachok.cryptotracker.crypto.presentation.coin_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.presentation.coin_home.coinPortfolioPrev
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.PriceChange
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinPortfolioCard(modifier: Modifier = Modifier, coinPortfolioUi: CoinPortfolioUi) {
    val cardShape = RoundedCornerShape(12.dp)
    Column(
        modifier
            .size(150.dp)
            .shadow(5.dp, cardShape, spotColor = MaterialTheme.colorScheme.primary)
            .background(MaterialTheme.colorScheme.surface)
            .clip(cardShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = cardShape)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = ImageVector.vectorResource(id = coinPortfolioUi.iconRes),
                contentDescription = coinPortfolioUi.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp)
            )
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = coinPortfolioUi.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = coinPortfolioUi.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = "$ ${coinPortfolioUi.priceUsd.formatted}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PriceChange(change = coinPortfolioUi.changePercent24Hr, fontSize = 12.sp, showArrow = false)
            Box(Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceContainer))
        }
    }
}

@PreviewLightDark
@Composable
private fun CPCPrev() {
    CryptoTrackerTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)) {
            CoinPortfolioCard(
                Modifier,
                coinPortfolioPrev.toCoinPortfolioUI()
            )
        }
    }
}