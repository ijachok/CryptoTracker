package me.ijachok.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ijachok.cryptotracker.core.presentation.util.ObserveAsEvents
import me.ijachok.cryptotracker.core.presentation.util.toString
import me.ijachok.cryptotracker.crypto.domain.CoinEvent
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_search.CoinSearchAction
import me.ijachok.cryptotracker.crypto.presentation.coin_search.CoinSearchScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_search.CoinSearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinSearchDetailPane(
    modifier: Modifier = Modifier,
    innerPaddingValues: PaddingValues,
    viewModel: CoinSearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CoinEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinSearchScreen(
                    state = state,
                    innerPadding = innerPaddingValues,
                    onAction = { action ->
                        viewModel.onAction(action)
                        when(action){
                            is CoinSearchAction.OnCoinClick ->{
                                navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail)
                            }
                            else -> {}
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane(){
                CoinDetailScreen(state.toListState(), Modifier.padding(innerPaddingValues))
            }
        }
    )
}