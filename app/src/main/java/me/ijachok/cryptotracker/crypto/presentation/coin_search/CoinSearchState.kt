package me.ijachok.cryptotracker.crypto.presentation.coin_search

import me.ijachok.cryptotracker.crypto.domain.CoinUi
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListState

data class CoinSearchState(
    val isLoading: Boolean = false,
    val isLoadingPreview: Boolean = false,
    val query:String = "",
    val coins: List<CoinUi> = emptyList(),
    val searchPreviewCoins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
){
    fun toListState() = CoinListState(isLoading, coins, selectedCoin)
}