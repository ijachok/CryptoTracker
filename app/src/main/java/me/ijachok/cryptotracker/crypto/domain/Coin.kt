package me.ijachok.cryptotracker.crypto.domain

data class Coin(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double? = 0.0,
    val priceUsd: Double,
    val changePercent24Hr: Double? = 0.0
)
