package me.ijachok.cryptotracker.crypto.presentation.coin_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import me.ijachok.cryptotracker.crypto.domain.CoinEvent
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.domain.toCoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinSearchViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(CoinSearchState())
    val state = _state.onStart {searchCoin("btc") }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinSearchState()
        )

    private val _events = Channel<CoinEvent>()
    val events = _events.receiveAsFlow()

    private var queryLastUpdated = System.currentTimeMillis()

    fun onAction(action: CoinSearchAction) {
        when (action) {
            is CoinSearchAction.OnCoinClick -> {
                selectCoin(action.coinUi)
            }

            is CoinSearchAction.OnCoinSearch -> searchCoin(action.query)
            is CoinSearchAction.OnQueryChange -> updateQuery(action.newQuery)
            is CoinSearchAction.OnCoinSearchPreview -> searchCoinPreview(action.query, action.limit)
        }
    }

    private fun selectCoin(coinUi: CoinUi) {
        _state.update { it.copy(selectedCoin = coinUi) }
        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coinUi.id,
                start = ZonedDateTime.now().minusDays(5),
                end = ZonedDateTime.now()
            )
                .onSuccess { history ->
                    val dataPoints = history
                        .sortedBy { it.dateTime }
                        .map {
                            DataPoint(
                                x = it.dateTime.hour.toFloat(),
                                y = it.priceUsd.toFloat(),
                                xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                            )
                        }
                    _state.update {
                        it.copy(selectedCoin = it.selectedCoin?.copy(coinPriceHistory = dataPoints))
                    }
                }
                .onError { error ->
                    _events.send(CoinEvent.Error(error))
                }
        }
    }

    private fun searchCoin(query: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            coinDataSource.searchCoins(query = query, limit = 100)
                .onSuccess { coins ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            coins = coins.map { it.toCoinUi() })
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CoinEvent.Error(error))
                }
        }
    }

    private fun searchCoinPreview(query: String, limit: Int) {
        _state.update { it.copy(isLoadingPreview = true) }
        viewModelScope.launch {
            coinDataSource.searchCoins(query = query, limit = limit)
                .onSuccess { coins ->
                    _state.update { state ->
                        state.copy(isLoadingPreview = false, searchPreviewCoins = coins.map { it.toCoinUi() })
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoadingPreview = false) }
                    _events.send(CoinEvent.Error(error))
                }
        }
    }

    private fun updateQuery(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }
}