package me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import me.ijachok.cryptotracker.crypto.domain.toDisplayableNumber
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.PriceChange
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.previewCoinUi
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun CoinPortfolioListItem(
    modifier: Modifier = Modifier,
    coinPortfolioUi: CoinPortfolioUi,
    inEditMode: Boolean,
    onCoinClick: () -> Unit,
    onCoinDelete: (CoinPortfolioUi) -> Unit,
    onCoinEdit: (CoinPortfolioUi) -> Unit,
) {
    val contentColor = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier
            .clickable(onClick = onCoinClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = coinPortfolioUi.iconRes),
            contentDescription = coinPortfolioUi.name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(54.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coinPortfolioUi.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = displayFontFamily,
                color = contentColor,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = coinPortfolioUi.name,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Light,
                color = contentColor,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Amount: ${coinPortfolioUi.amount.formatted}",
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Light,
                color = contentColor,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(16.dp))


        AnimatedContent(
            targetState = inEditMode,
            transitionSpec = {
                (fadeIn(tween(300)) + slideInHorizontally { it })
                    .togetherWith(fadeOut(tween(300)) + slideOutHorizontally { -it })
            }

        ) { editing ->
            if (editing)
                Row {
                    IconButton(
                        onClick = { onCoinEdit(coinPortfolioUi) }
                    ) {
                        Icon(
                            Icons.Rounded.Edit,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = { onCoinDelete(coinPortfolioUi) }
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            else
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = (coinPortfolioUi.priceUsd.value * coinPortfolioUi.amount.value).toDisplayableNumber().formatted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = displayFontFamily,
                        color = contentColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceChange(
                        change = coinPortfolioUi.changePercent24Hr
                    )
                }
        }
    }
}

@PreviewLightDark
@Composable
private fun CPLIPrev() {
    CryptoTrackerTheme {
        CoinPortfolioListItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            coinPortfolioUi = CoinPortfolioUi(
                id = previewCoinUi.id,
                rank = previewCoinUi.rank,
                name = previewCoinUi.name,
                symbol = previewCoinUi.symbol,
                amount = 1.5.toDisplayableNumber(),
                marketCapUsd = previewCoinUi.marketCapUsd,
                priceUsd = previewCoinUi.priceUsd,
                changePercent24Hr = previewCoinUi.changePercent24Hr,
                iconRes = previewCoinUi.iconRes,
                coinPriceHistory = previewCoinUi.coinPriceHistory
            ),
            inEditMode = false,
            onCoinClick = {},
            onCoinDelete = {},
            onCoinEdit = {},

            )
    }
}

@PreviewLightDark
@Composable
private fun CPLIPrevEditMode() {
    CryptoTrackerTheme {
        CoinPortfolioListItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            coinPortfolioUi = CoinPortfolioUi(
                id = previewCoinUi.id,
                rank = previewCoinUi.rank,
                name = previewCoinUi.name,
                symbol = previewCoinUi.symbol,
                amount = 1.5.toDisplayableNumber(),
                marketCapUsd = previewCoinUi.marketCapUsd,
                priceUsd = previewCoinUi.priceUsd,
                changePercent24Hr = previewCoinUi.changePercent24Hr,
                iconRes = previewCoinUi.iconRes,
                coinPriceHistory = previewCoinUi.coinPriceHistory
            ),
            inEditMode = true,
            onCoinClick = {},
            onCoinDelete = {},
            onCoinEdit = {},
        )
    }
}