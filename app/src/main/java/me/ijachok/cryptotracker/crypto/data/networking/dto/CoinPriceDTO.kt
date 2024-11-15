package me.ijachok.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDTO(
    val priceUsd:Double,
    val time: Long
)