package me.ijachok.cryptotracker.crypto.domain

import me.ijachok.cryptotracker.core.domain.util.NetworkError

sealed interface CoinEvent {
    data class Error(val error:NetworkError): CoinEvent
}