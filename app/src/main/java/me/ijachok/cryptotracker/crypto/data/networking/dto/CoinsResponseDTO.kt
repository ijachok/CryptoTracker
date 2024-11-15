package me.ijachok.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinsResponseDTO(
    val data:List<CoinDTO>
)