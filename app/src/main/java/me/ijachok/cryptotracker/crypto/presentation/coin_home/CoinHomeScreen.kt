package me.ijachok.cryptotracker.crypto.presentation.coin_home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowOutward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.crypto.data.mappers.toCoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.DisplayableNumber
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.domain.toDisplayableNumber
import me.ijachok.cryptotracker.crypto.presentation.coin_home.components.CoinPortfolioCard
import me.ijachok.cryptotracker.crypto.presentation.coin_home.components.CoinPortfolioListItem
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.bodyFontFamily
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun CoinHomeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: CoinHomeState,
    onAction: (CoinHomeAction) -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            Color(0x00ffffff)
                        ),
                        center = Offset.Zero
                    )
                )
        )
        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(16.dp))
            TotalBalanceSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                balance = state.balance.toDisplayableNumber()
            )
            Spacer(Modifier.height(16.dp))
            CoinCardsSection(
                Modifier
                    .fillMaxWidth(),
                portfolioCoins = state.portfolio,
                contentPaddingValues = PaddingValues(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            PortfolioSection(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                state.portfolio,
                state.balance
            ){

            }
            Button(onClick = {
                onAction(CoinHomeAction.OnPortfolioEdit(
                    coinPortfolioListPrev.map { it.toCoinAmount() }
                ))
            }) { Text("Insert test portfolio") }
        }
    }

}

@Composable
fun TotalBalanceSection(modifier: Modifier = Modifier, balance: DisplayableNumber) {
    Column(
        modifier
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.total_balance),
            fontFamily = displayFontFamily,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "$ ${balance.formatted}",
            fontFamily = bodyFontFamily,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

    }
}

@Composable
fun CoinCardsSection(
    modifier: Modifier = Modifier,
    portfolioCoins: List<CoinPortfolioUi>,
    contentPaddingValues: PaddingValues = PaddingValues()
) {
    LazyRow(
        modifier = modifier,
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPaddingValues
    ) {
        items(
            items = portfolioCoins,
            key = { coinPortfolio -> coinPortfolio.symbol }) { coinPortfolioUi ->
            CoinPortfolioCard(Modifier, coinPortfolioUi)
        }
    }
}

@Composable
fun PortfolioSection(
    modifier: Modifier = Modifier,
    portfolioCoins: List<CoinPortfolioUi>,
    totalBalance: Double,
    onClick:() -> Unit
) {
    val cardShape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .shadow(5.dp, cardShape, spotColor = MaterialTheme.colorScheme.primary)
            .background(MaterialTheme.colorScheme.surface)
            .clip(cardShape)
            .clickable { onClick() }
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = cardShape)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.portfolio),
                fontFamily = displayFontFamily,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Rounded.ArrowOutward,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (coinPortfolio in portfolioCoins.take(5)) {
                CoinPortfolioListItem(Modifier, coinPortfolio, totalBalance)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CHSPrev() {
    CryptoTrackerTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CoinHomeScreen(
                Modifier,
                paddingValues = PaddingValues(),
                CoinHomeState(
                    false,
                    29283744198.0,
                    coinPortfolioListPrev.map { it.toCoinPortfolioUI() }
                )
            ) { }
        }
    }
}

val coinPortfolioPrev = CoinPortfolio(
    id = "bitcoin",
    rank = 1,
    name = "Bitcoin",
    symbol = "BTC",
    amountOwned = 123.534,
    marketCapUsd = 1241273958896.75,
    priceUsd = 62828.15,
    changePercent24Hr = -0.1
)

val coinPortfolioListPrev = listOf(
    CoinPortfolio(
        id = "bitcoin",
        rank = 1,
        name = "Bitcoin",
        symbol = "BTC",
        amountOwned = 121132.534,
        marketCapUsd = 1241273958896.75,
        priceUsd = 62828.15,
        changePercent24Hr = -0.1
    ),
    CoinPortfolio(
        id = "ethereum",
        rank = 2,
        name = "Ethereum",
        symbol = "ETH",
        amountOwned = 452113.332,
        marketCapUsd = 520334789653.12,
        priceUsd = 3452.34,
        changePercent24Hr = 0.45
    ),
    CoinPortfolio(
        id = "cardano",
        rank = 3,
        name = "Cardano",
        symbol = "ADA",
        amountOwned = 9831124.21,
        marketCapUsd = 67894321654.45,
        priceUsd = 2.17,
        changePercent24Hr = 1.25
    ),
    CoinPortfolio(
        id = "solana",
        rank = 4,
        name = "Solana",
        symbol = "SOL",
        amountOwned = 2391185.45,
        marketCapUsd = 51278496321.89,
        priceUsd = 216.89,
        changePercent24Hr = -0.4
    ),
    CoinPortfolio(
        id = "polkadot",
        rank = 5,
        name = "Polkadot",
        symbol = "DOT",
        amountOwned = 32156.78,
        marketCapUsd = 43129654328.67,
        priceUsd = 42.89,
        changePercent24Hr = 2.3
    ),
    CoinPortfolio(
        id = "dogecoin",
        rank = 6,
        name = "Dogecoin",
        symbol = "DOGE",
        amountOwned = 1234567.89,
        marketCapUsd = 30786123543.12,
        priceUsd = 0.24,
        changePercent24Hr = -1.1
    ),
    CoinPortfolio(
        id = "litecoin",
        rank = 9,
        name = "Litecoin",
        symbol = "LTC",
        amountOwned = 76543.21,
        marketCapUsd = 15348765321.65,
        priceUsd = 202.65,
        changePercent24Hr = 1.75
    ),
    CoinPortfolio(
        id = "chainlink",
        rank = 10,
        name = "Chainlink",
        symbol = "LINK",
        amountOwned = 34567.89,
        marketCapUsd = 14563215478.54,
        priceUsd = 23.54,
        changePercent24Hr = 2.9
    )
)

