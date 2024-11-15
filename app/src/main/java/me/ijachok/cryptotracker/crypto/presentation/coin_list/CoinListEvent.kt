package me.ijachok.cryptotracker.crypto.presentation.coin_list

import me.ijachok.cryptotracker.core.domain.util.NetworkError

sealed interface CoinListEvent {
    data class Error(val error:NetworkError):CoinListEvent
}