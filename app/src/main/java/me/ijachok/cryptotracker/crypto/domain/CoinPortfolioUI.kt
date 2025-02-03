package me.ijachok.cryptotracker.crypto.domain

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import me.ijachok.cryptotracker.core.presentation.util.getDrawableIdForCoin
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint

@Immutable
data class CoinPortfolioUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val amount: DisplayableNumber,
    val marketCapUsd: DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
    val coinPriceHistory: List<DataPoint> = emptyList()
)

fun CoinPortfolio.toCoinPortfolioUI(): CoinPortfolioUi {
    return CoinPortfolioUi(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        amount = amountOwned.toDisplayableNumber(),
        priceUsd = priceUsd.toDisplayableNumber(),
        marketCapUsd = (marketCapUsd ?: 0.0).toDisplayableNumber(),
        changePercent24Hr = (changePercent24Hr ?: 0.0).toDisplayableNumber(),
        iconRes = getDrawableIdForCoin(symbol)
    )
}

fun CoinPortfolioUi.toCoinUi(): CoinUi =
    CoinUi(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        iconRes = iconRes,
        coinPriceHistory = coinPriceHistory

    )

