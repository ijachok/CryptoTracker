package me.ijachok.cryptotracker.crypto.domain

import android.icu.text.NumberFormat
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import me.ijachok.cryptotracker.core.presentation.util.getDrawableIdForCoin
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint
import java.util.Locale

@Immutable
data class CoinUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
    val coinPriceHistory:List<DataPoint> = emptyList()
)

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)

fun Coin.toCoinUi(): CoinUi {
    return CoinUi(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUsd = priceUsd.toDisplayableNumber(),
        marketCapUsd = (marketCapUsd?:0.0).toDisplayableNumber(),
        changePercent24Hr = (changePercent24Hr?:0.0).toDisplayableNumber(),
        iconRes = getDrawableIdForCoin(symbol)
    )
}

fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this,
        formatted = formatter.format(this)
    )
}