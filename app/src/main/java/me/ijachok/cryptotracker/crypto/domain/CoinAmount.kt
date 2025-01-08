package me.ijachok.cryptotracker.crypto.domain

data class CoinAmount(
    val id: String,
    val name: String,
    val symbol: String,
    val amountOwned:Double = 0.0,
)