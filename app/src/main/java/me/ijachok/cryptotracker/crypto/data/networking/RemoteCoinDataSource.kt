package me.ijachok.cryptotracker.crypto.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import me.ijachok.cryptotracker.core.data.networking.constructURL
import me.ijachok.cryptotracker.core.data.networking.safeCall
import me.ijachok.cryptotracker.core.domain.CoinDataSource
import me.ijachok.cryptotracker.core.domain.util.NetworkError
import me.ijachok.cryptotracker.core.domain.util.Result
import me.ijachok.cryptotracker.core.domain.util.map
import me.ijachok.cryptotracker.crypto.data.mappers.toCoin
import me.ijachok.cryptotracker.crypto.data.mappers.toCoinPrice
import me.ijachok.cryptotracker.crypto.data.networking.dto.CoinHistoryDTO
import me.ijachok.cryptotracker.crypto.data.networking.dto.CoinsResponseDTO
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPrice
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDTO> {
            httpClient.get(
                urlString = constructURL("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val endMillis = end.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()

        return safeCall<CoinHistoryDTO> {
            httpClient.get(
                urlString = constructURL("/assets/$coinId/history")
            ) {
                parameter("interval", "h6")
                parameter("start", startMillis)
                parameter("end", endMillis)
            }
        }.map { response -> response.data.map { it.toCoinPrice() } }
    }

    override suspend fun searchCoins(query: String, limit: Int): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDTO> {
            httpClient.get(
                urlString = constructURL("/assets")
            ){
                parameter("search", query)
                parameter("limit", limit)
            }
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }
}