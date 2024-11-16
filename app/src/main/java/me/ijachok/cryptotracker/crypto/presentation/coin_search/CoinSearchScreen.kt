package me.ijachok.cryptotracker.crypto.presentation.coin_search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.core.presentation.util.getDrawableIdForCoin
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.CoinListItem
import me.ijachok.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import me.ijachok.cryptotracker.crypto.presentation.coin_search.components.CryptoSearchBar
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.displayFontFamily

@Composable
fun CoinSearchScreen(
    modifier: Modifier = Modifier,
    innerPadding:PaddingValues,
    state: CoinSearchState,
    onAction: (CoinSearchAction) -> Unit
) {

    Box(Modifier.fillMaxSize()){
        var expanded by remember { mutableStateOf(false) }

        LaunchedEffect(state.query) {
            if (state.query.isNotEmpty()) {
                delay(500)
                onAction(CoinSearchAction.OnCoinSearchPreview(state.query, 4))
            }
        }
        val searchBarPadding by animateDpAsState(
            targetValue = if (expanded) 0.dp else 16.dp, tween(200),
            label = ""
        )

        CryptoSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal =  searchBarPadding),
            query = state.query,
            expanded = expanded,
            onExpandedChange = { expanded = it },
            onSearch = {
                expanded = false
                onAction(CoinSearchAction.OnCoinSearch(state.query))
            },
            onQueryChange = { onAction(CoinSearchAction.OnQueryChange(it)) },
//            leadingIcon = {
//                IconButton(onClick = {}) {
//                    Icon(
//                        imageVector = Icons.Outlined.Menu,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onBackground
//                    )
//                }
//            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            }

        ) {

            if (state.isLoadingPreview) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        Modifier.height(40.dp)
                    )
                }
            } else if (state.searchPreviewCoins.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = stringResource(R.string.no_results),
                    textAlign = TextAlign.Center
                )
            } else
                state.searchPreviewCoins.forEachIndexed { index, coinUi ->
                    ListItem(
                        modifier = Modifier.clickable {
                            onAction(CoinSearchAction.OnCoinClick(coinUi))
                        },
                        headlineContent = {
                            Text(
                                text = coinUi.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = displayFontFamily
                            )
                        },
                        supportingContent = { Text(coinUi.symbol) },
                        leadingContent = {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(getDrawableIdForCoin(coinUi.symbol)),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
        }
        Column(
            modifier = modifier.fillMaxSize()
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
                LazyColumn(
                    state = state.listState,
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = innerPadding.calculateTopPadding() + 72.dp)
                ) {
                    items(state.coins) { coinUi ->
                        CoinListItem(
                            coinUi = coinUi,
                            onClick = { onAction(CoinSearchAction.OnCoinClick(coinUi)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinSearchScreenPreview() {
    CryptoTrackerTheme {
        CoinSearchScreen(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            innerPadding = PaddingValues(),
            state = CoinSearchState(
                coins = (1..100).map {
                    previewCoin.copy(id = it.toString())
                },
                searchPreviewCoins = (1..5).map {
                    previewCoin.copy(id = it.toString())
                }
            ),
            onAction = {}
        )
    }
}