package me.ijachok.cryptotracker.crypto.domain

data class CoinPortfolio(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val amountOwned:Double,
    val marketCapUsd: Double? = 0.0,
    val priceUsd: Double,
    val changePercent24Hr: Double? = 0.0
)
