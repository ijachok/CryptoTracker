package me.ijachok.cryptotracker.crypto.presentation.coin_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.domain.toDisplayableNumber
import me.ijachok.cryptotracker.crypto.presentation.coin_home.coinPortfolioPrev
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.PriceChange
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinPortfolioListItem(
    modifier: Modifier = Modifier,
    coinPortfolioUi: CoinPortfolioUi,
    totalBalance: Double
) {

    val percentage =
        (((coinPortfolioUi.amount.value * coinPortfolioUi.priceUsd.value) / totalBalance) * 100).toDisplayableNumber(
            1
        )

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = ImageVector.vectorResource(id = coinPortfolioUi.iconRes),
            contentDescription = coinPortfolioUi.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(44.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier
        ) {
            Row(
                Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = coinPortfolioUi.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${percentage.formatted}%",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = coinPortfolioUi.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.weight(1f))
                PriceChange(Modifier, coinPortfolioUi.changePercent24Hr, fontSize = 12.sp)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CPLIPrev() {
    CryptoTrackerTheme {
        CoinPortfolioListItem(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            coinPortfolioPrev.toCoinPortfolioUI(),
            19477193.0
        )
    }
}