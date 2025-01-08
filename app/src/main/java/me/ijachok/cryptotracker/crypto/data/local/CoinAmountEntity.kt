package me.ijachok.cryptotracker.crypto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.ijachok.cryptotracker.crypto.domain.CoinAmount

@Entity(tableName = "owned_coins")
data class CoinAmountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val amountOwned: Double,
)

fun CoinAmountEntity.toCoinAmount(): CoinAmount {
    return CoinAmount(
        id = id,
        name = name,
        symbol = symbol,
        amountOwned = amountOwned
    )
}
