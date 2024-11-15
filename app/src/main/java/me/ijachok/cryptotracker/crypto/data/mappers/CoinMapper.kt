package me.ijachok.cryptotracker.crypto.data.mappers

import me.ijachok.cryptotracker.crypto.data.networking.dto.CoinDTO
import me.ijachok.cryptotracker.crypto.data.networking.dto.CoinPriceDTO
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId

fun CoinDTO.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr

    )
}

fun CoinPriceDTO.toCoinPrice(): CoinPrice {
    return CoinPrice(
        priceUsd = priceUsd,
        dateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
    )
}