package me.ijachok.cryptotracker.crypto.presentation.coin_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.CoinListItem
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.TopLabelBar
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListScreen(
    modifier: Modifier = Modifier,
    state: CoinListState,
    onAction: (CoinListAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopLabelBar {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onSurface
//            )
        }
        if(state.isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            LazyColumn(
                state = state.listState,
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.coins) { coinUi ->
                    CoinListItem(
                        coinUi = coinUi,
                        onClick = { onAction(CoinListAction.OnCoinClick(coinUi)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}

@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    CryptoTrackerTheme {
        CoinListScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            state = CoinListState(
                coins = (1..100).map {
                    previewCoin.copy(id = it.toString())
                }
            ),
            onAction = {}
        )
    }
}