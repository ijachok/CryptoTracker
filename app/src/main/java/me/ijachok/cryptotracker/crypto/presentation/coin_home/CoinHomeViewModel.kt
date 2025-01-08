package me.ijachok.cryptotracker.crypto.presentation.coin_home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.ijachok.cryptotracker.core.domain.CoinDataSource
import me.ijachok.cryptotracker.core.domain.util.onError
import me.ijachok.cryptotracker.core.domain.util.onSuccess
import me.ijachok.cryptotracker.crypto.data.local.LocalCoinDataSource
import me.ijachok.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import me.ijachok.cryptotracker.crypto.domain.CoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinEvent
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI

class CoinHomeViewModel(
    private val localCoinDataSource: LocalCoinDataSource,
    private val remoteCoinDataSource: CoinDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(CoinHomeState())
    val state = _state.onStart { loadCoinPortfolio() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinHomeState()
        )

    private val _events = Channel<CoinEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinHomeAction) {
        when (action) {
            is CoinHomeAction.OnCoinClick -> TODO()
            is CoinHomeAction.OnPortfolioEdit -> {
                editPortfolio(action.coinAmounts)
            }
            CoinHomeAction.OnPortfolioOpen -> TODO()
        }
    }

    private fun loadCoinPortfolio() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            localCoinDataSource.getCoinAmounts()
                .onSuccess { coinAmounts ->
                    remoteCoinDataSource.getCoinPortfolio(coinAmounts)
                        .onSuccess { portfolio ->
                            _state.update { oldState ->
                                oldState.copy(
                                    isLoading = false,
                                    balance = calculateBalance(portfolio),
                                    portfolio = portfolio.map { it.toCoinPortfolioUI() })
                            }
                        }
                        .onError { error ->
                            _events.send(CoinEvent.NetError(error))
                        }
                }
                .onError {
                    _events.send(CoinEvent.LocalDataError(it))
                }
        }
    }

    private fun editPortfolio(coinAmounts:List<CoinAmount>){
        viewModelScope.launch(Dispatchers.IO) {
            localCoinDataSource.insertCoins(coinAmounts)
        }
    }

    private fun calculateBalance(coinPortfolio: List<CoinPortfolio>): Double {

        return coinPortfolio.sumOf { it.priceUsd * it.amountOwned }
    }
}