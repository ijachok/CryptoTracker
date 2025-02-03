package me.ijachok.cryptotracker.crypto.data.local

import android.util.Log
import me.ijachok.cryptotracker.core.data.local.LocalDatabase
import me.ijachok.cryptotracker.core.domain.PortfolioDataSource
import me.ijachok.cryptotracker.core.domain.util.LocalDatabaseError
import me.ijachok.cryptotracker.core.domain.util.Result
import me.ijachok.cryptotracker.crypto.data.mappers.toCoinEntity
import me.ijachok.cryptotracker.crypto.domain.CoinAmount

class LocalCoinDataSource(private val localDatabase: LocalDatabase) : PortfolioDataSource {

    override suspend fun insertCoins(coins: List<CoinAmount>) {
        localDatabase.coinDao().insertCoins(coins.map { it.toCoinEntity()})
    }
    override suspend fun getCoinAmounts(): Result<List<CoinAmount>, LocalDatabaseError> {
        try {
            return Result.Success(localDatabase.coinDao().getAllCoins().map { it.toCoinAmount() })
        }catch (e:Exception){
            Log.d("abba", "getCoinAmounts: ${e.message}")
            return Result.Error(LocalDatabaseError.UNKNOWN)
        }
    }

    override suspend fun deleteCoin(coinId: String) {
        localDatabase.coinDao().deleteCoin(coinId)
    }

}