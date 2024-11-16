package me.ijachok.cryptotracker.core.domain

import me.ijachok.cryptotracker.core.domain.util.NetworkError
import me.ijachok.cryptotracker.core.domain.util.Result
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPrice
import java.time.ZonedDateTime

interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
    suspend fun getCoinHistory(
        coinId:String,
        start:ZonedDateTime,
        end:ZonedDateTime
    ):Result<List<CoinPrice>, NetworkError>

    suspend fun searchCoins(query:String, limit:Int):Result<List<Coin>, NetworkError>
}