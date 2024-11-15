package me.ijachok.cryptotracker.crypto.presentation.coin_list

import me.ijachok.cryptotracker.crypto.presentation.coin_list.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coinUI: CoinUi) : CoinListAction
    data object OnRefresh : CoinListAction
}