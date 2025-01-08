package me.ijachok.cryptotracker.core.domain

import me.ijachok.cryptotracker.core.domain.util.LocalDatabaseError
import me.ijachok.cryptotracker.core.domain.util.Result
import me.ijachok.cryptotracker.crypto.domain.CoinAmount
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio

interface PortfolioDataSource {
    suspend fun insertCoins(coins: List<CoinAmount>)
    suspend fun getCoinAmounts():Result<List<CoinAmount>, LocalDatabaseError>
}