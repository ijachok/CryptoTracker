package me.ijachok.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinResponseDTO(
    val data:CoinDTO
)