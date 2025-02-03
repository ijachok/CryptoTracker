package me.ijachok.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.ijachok.cryptotracker.core.presentation.util.ObserveAsEvents
import me.ijachok.cryptotracker.core.presentation.util.toString
import me.ijachok.cryptotracker.crypto.domain.CoinEvent
import me.ijachok.cryptotracker.crypto.presentation.coin_home.CoinHomeScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_home.CoinHomeViewModel
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.CoinPortfolioScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeTab(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val coinHomeViewModel: CoinHomeViewModel = koinViewModel()
    val homeState by coinHomeViewModel.homeState.collectAsStateWithLifecycle()
    val portfolioState by coinHomeViewModel.portfolioState.collectAsStateWithLifecycle()
    val portfolioCoinHintList by coinHomeViewModel.coinListByQuery.collectAsStateWithLifecycle()

    ObserveAsEvents(coinHomeViewModel.coinEvents) { event ->
        when (event) {
            is CoinEvent.NetError -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is CoinEvent.LocalDataError -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    val navController = rememberNavController()
    NavHost(navController, startDestination = HomeDestination) {
        composable<HomeDestination> {
            CoinHomeScreen(
                modifier = Modifier,
                paddingValues = paddingValues,
                state = homeState,
                coinHintList = portfolioCoinHintList,
                onPortfolioClick = {
                    navController.navigate(PortfolioDestination)
                }
            ) { action ->
                coinHomeViewModel.onAction(action)
            }
        }

        composable<PortfolioDestination> {
            CoinPortfolioScreen(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                state = portfolioState,
                coinHintList = portfolioCoinHintList,
                coinPortfolioEvents = coinHomeViewModel.coinPortfolioEvents,
                onBackClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() },
                onAction = { action ->
                    coinHomeViewModel.onAction(action)
                }
            )

        }

    }


}