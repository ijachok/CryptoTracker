package me.ijachok.cryptotracker.crypto.domain

import me.ijachok.cryptotracker.core.domain.util.LocalDatabaseError
import me.ijachok.cryptotracker.core.domain.util.NetworkError

sealed interface CoinEvent {
    data class NetError(val error:NetworkError): CoinEvent
    data class LocalDataError(val error:LocalDatabaseError): CoinEvent
}