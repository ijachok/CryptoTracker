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
import me.ijachok.cryptotracker.crypto.data.mappers.toCoinAmount
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinEvent
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioEvent
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.domain.toCoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.CoinPortfolioAction
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.CoinPortfolioState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinHomeViewModel(
    private val localCoinDataSource: LocalCoinDataSource,
    private val remoteCoinDataSource: CoinDataSource,
) : ViewModel() {

    private val _homeState = MutableStateFlow(CoinHomeState())
    val homeState = _homeState.onStart { loadCoinPortfolio() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinHomeState()
        )

    private val _portfolioState = MutableStateFlow(CoinPortfolioState())
    val portfolioState = _portfolioState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        CoinPortfolioState()
    )

    private val _coinHintList = MutableStateFlow<List<Coin>>(listOf())
    val coinListByQuery = _coinHintList.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        listOf()
    )

    private val _coinEvents = Channel<CoinEvent>()
    val coinEvents = _coinEvents.receiveAsFlow()
    private val _coinPortfolioEvents = Channel<CoinPortfolioEvent>()
    val coinPortfolioEvents = _coinPortfolioEvents.receiveAsFlow()

    fun onAction(action: CoinHomeAction) {
        when (action) {
            is CoinHomeAction.OnCoinClick -> selectCoin(action.coinUI)
            is CoinHomeAction.OnPortfolioEdit -> insertDemoPortfolio(action.coinAmounts)
            is CoinHomeAction.OnCoinAdd -> addCoin(action.coinPortfolio)
            is CoinHomeAction.OnCoinSearchHitList -> searchCoins(action.query)
        }
    }

    fun onAction(action: CoinPortfolioAction) {
        when (action) {
            is CoinPortfolioAction.OnCoinClick -> selectCoin(action.coinPortfolioUi.toCoinUi())
            CoinPortfolioAction.OnPortfolioEdit -> editPortfolio()
            CoinPortfolioAction.OnPortfolioRefresh -> loadCoinPortfolio()
            CoinPortfolioAction.OnPortfolioFinishEdit -> finishEditPortfolio()
            is CoinPortfolioAction.OnCoinDelete -> deleteCoin(action.coinId)
            is CoinPortfolioAction.OnCoinEdit -> changeCoin(action.coinPortfolioUi)
            is CoinPortfolioAction.OnCoinSearchHitList -> searchCoins(action.query)
            is CoinPortfolioAction.OnCoinAdd -> addCoin(action.coinPortfolio)
        }

    }

    private fun loadCoinPortfolio() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.update { it.copy(isLoading = true) }
            _portfolioState.update { it.copy(isLoading = true) }
            localCoinDataSource.getCoinAmounts()
                .onSuccess { coinAmounts ->
                    remoteCoinDataSource.getCoinPortfolio(coinAmounts)
                        .onSuccess { portfolio ->
                            _homeState.update { oldState ->
                                oldState.copy(
                                    isLoading = false,
                                    portfolio = portfolio.map { it.toCoinPortfolioUI() })
                            }
                            _portfolioState.update { oldState ->
                                oldState.copy(
                                    isLoading = false,
                                    portfolio = portfolio.map { it.toCoinPortfolioUI() })
                            }
                        }
                        .onError { error ->
                            _coinEvents.send(CoinEvent.NetError(error))
                        }
                }
                .onError {
                    _coinEvents.send(CoinEvent.LocalDataError(it))
                }
        }
    }

    private fun selectCoin(coinUi: CoinUi) {
        _homeState.update { it.copy(selectedCoin = coinUi) }
        _portfolioState.update { it.copy(selectedCoin = coinUi) }
        viewModelScope.launch {
            remoteCoinDataSource.getCoinHistory(
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
                    _homeState.update {
                        it.copy(selectedCoin = it.selectedCoin?.copy(coinPriceHistory = dataPoints))
                    }
                    _portfolioState.update {
                        it.copy(selectedCoin = it.selectedCoin?.copy(coinPriceHistory = dataPoints))
                    }
                }
                .onError { error ->
                    _coinEvents.send(CoinEvent.NetError(error))
                }
        }
    }

    private fun insertDemoPortfolio(coinAmounts: List<CoinAmount>) {
        viewModelScope.launch(Dispatchers.IO) {
            localCoinDataSource.insertCoins(coinAmounts)
        }
    }

    private fun editPortfolio() {
        _portfolioState.update { oldState ->
            oldState.copy(inEditMode = true)
        }
    }

    private fun finishEditPortfolio() {
        _portfolioState.update { oldState ->
            oldState.copy(inEditMode = false)
        }
    }

    private fun searchCoins(query: String) {
        _coinHintList.value = listOf()
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                remoteCoinDataSource.searchCoins(query = query, limit = 3)
                    .onSuccess { coins ->
                        _coinHintList.value = coins
                    }
                    .onError { error ->
                        _coinEvents.send(CoinEvent.NetError(error))
                    }
            }
        }
    }

    private fun addCoin(coinPortfolio: CoinPortfolio) {
        viewModelScope.launch {
            if (_portfolioState.value.portfolio.any { it.id == coinPortfolio.id }) {
                _coinPortfolioEvents.send(CoinPortfolioEvent.CoinAlreadyInList(coinPortfolio))
            } else {
                localCoinDataSource.insertCoins(listOf(coinPortfolio.toCoinAmount()))

                _portfolioState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList() + coinPortfolio.toCoinPortfolioUI()
                    )
                }
                _homeState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList() + coinPortfolio.toCoinPortfolioUI()
                    )
                }
            }
        }
    }

    private fun changeCoin(coinPortfolioUi: CoinPortfolioUi) {
        viewModelScope.launch {
            val coinIndex = _portfolioState.value.portfolio.indexOfFirst { it.id == coinPortfolioUi.id }
            if (coinIndex > -1) {
                localCoinDataSource.insertCoins(
                    listOf(
                        CoinAmount(
                            id = coinPortfolioUi.id,
                            name = coinPortfolioUi.name,
                            symbol = coinPortfolioUi.symbol,
                            amountOwned = coinPortfolioUi.amount.value
                        )
                    )
                )
                _portfolioState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList().apply { this[coinIndex] = coinPortfolioUi }
                    )
                }
                _homeState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList().apply { this[coinIndex] = coinPortfolioUi }
                    )
                }
            } else {
                _coinPortfolioEvents.send(CoinPortfolioEvent.CoinDoesntExist)
            }
        }
    }

    private fun deleteCoin(coinId:String) {
        viewModelScope.launch {
            val coinIndex = _portfolioState.value.portfolio.indexOfFirst { it.id == coinId }
            if (coinIndex > -1) {
                localCoinDataSource.deleteCoin(coinId)
                _portfolioState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList().apply { this.removeAt(coinIndex) }
                    )
                }
                _homeState.update {
                    it.copy(
                        portfolio = it.portfolio.toMutableList().apply { this.removeAt(coinIndex) }
                    )
                }
            } else {
                _coinPortfolioEvents.send(CoinPortfolioEvent.CoinDoesntExist)
            }
        }
    }

    private fun calculateBalance(coinPortfolio: List<CoinPortfolio>): Double =
        coinPortfolio.sumOf { it.priceUsd * it.amountOwned }

}