package me.ijachok.cryptotracker.crypto.presentation.coin_list

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
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.domain.toCoinUi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state.onStart { loadCoins() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )

    private val _events = Channel<CoinEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {
                selectCoin(action.coinUI)
            }

            is CoinListAction.OnRefresh -> {
                loadCoins()
            }
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

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            coinDataSource.getCoins()
                .onSuccess { coins ->
                    _state.update { coinListState ->
                        coinListState.copy(isLoading = false, coins = coins.map { it.toCoinUi() })
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CoinEvent.Error(error))
                }
        }
    }
}