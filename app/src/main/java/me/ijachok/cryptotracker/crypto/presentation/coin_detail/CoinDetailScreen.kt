package me.ijachok.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.components.InfoCard
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.components.InfoCardSize
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.components.LineChart
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListState
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import me.ijachok.cryptotracker.crypto.presentation.coin_list.models.toDisplayableNumber
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.bodyFontFamily
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    state: CoinListState,
    modifier: Modifier = Modifier
) {
    val textColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.selectedCoin != null) {
        val coin = state.selectedCoin
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = coin.iconRes
                ),
                contentDescription = coin.name,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = coin.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                fontFamily = displayFontFamily,
                textAlign = TextAlign.Center,
                color = textColor
            )
            Text(
                text = coin.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = textColor
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InfoCard(
                    title = stringResource(id = R.string.market_cap),
                    formattedText = "$ ${coin.marketCapUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.stock),
                    cardSize = InfoCardSize.BIG
                )
                InfoCard(
                    title = stringResource(id = R.string.price),
                    formattedText = "$ ${coin.priceUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.dollar)
                )
                val absoluteChangeFormatted =
                    (coin.priceUsd.value * (coin.changePercent24Hr.value / 100))
                        .toDisplayableNumber()
                val isPositive = coin.changePercent24Hr.value > 0.0
                val contentColor = if (isPositive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
                InfoCard(
                    title = stringResource(id = R.string.change_last_24h),
                    formattedText = absoluteChangeFormatted.formatted,
                    icon = if (isPositive) {
                        ImageVector.vectorResource(id = R.drawable.trending)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.trending_down)
                    },
                    contentColor = contentColor
                )


            }
            AnimatedVisibility(visible = coin.coinPriceHistory.isNotEmpty()) {
                var selectedDataPoint by remember { mutableStateOf<DataPoint?>(null) }
                var labelWidth by remember { mutableFloatStateOf(0f) }
                var chartWidth by remember { mutableFloatStateOf(0f) }
                val visibleDataPointsCount = if(labelWidth >0){
                    ((chartWidth - 2.5 * labelWidth)/labelWidth).toInt()
                }else 0
                val startIndex = (coin.coinPriceHistory.lastIndex - visibleDataPointsCount).coerceAtLeast(0)
                LineChart(modifier = Modifier.padding(16.dp)
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .onSizeChanged { chartWidth = it.width.toFloat() },
                    dataPoints = coin.coinPriceHistory,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.tertiary,
                        unselectedColor = MaterialTheme.colorScheme.outline,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThicknessPx = 1f,
                        chartLineThicknessPx = 5f,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontFamily = bodyFontFamily
                        ),
                        minYLabelSpacing = 20.dp,
                        verticalPadding = 4.dp,
                        horizontalPadding = 4.dp,
                        xAxisLabelSpacing = 4.dp
                    ),
                    visibleDataPointsIndices = startIndex..coin.coinPriceHistory.lastIndex,
                    unitSign = "$",
                    showHelperLines = true,
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = { selectedDataPoint = it },
                    onXLabelWidthChange = {labelWidth = it}
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinListState(
                selectedCoin = previewCoin,
            ),
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background
            )
        )
    }
}