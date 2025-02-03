package me.ijachok.cryptotracker.crypto.domain

sealed interface CoinPortfolioEvent {
    data class CoinAlreadyInList(val coinPortfolio: CoinPortfolio): CoinPortfolioEvent
    data object CoinDoesntExist:CoinPortfolioEvent
}