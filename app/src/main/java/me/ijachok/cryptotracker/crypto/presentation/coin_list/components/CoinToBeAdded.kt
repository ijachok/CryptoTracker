package me.ijachok.cryptotracker.crypto.presentation.coin_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.toDisplayableNumber
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun CoinToBeAdded(modifier: Modifier = Modifier, coinPortfolioUi: CoinPortfolioUi) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(52.dp),
            imageVector = ImageVector.vectorResource(coinPortfolioUi.iconRes),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = coinPortfolioUi.name
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
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = coinPortfolioUi.name,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "$ ${(coinPortfolioUi.amount.value * coinPortfolioUi.priceUsd.value).toDisplayableNumber().formatted}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = displayFontFamily,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
    }
}