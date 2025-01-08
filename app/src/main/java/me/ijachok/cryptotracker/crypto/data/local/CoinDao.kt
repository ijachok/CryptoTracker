package me.ijachok.cryptotracker.crypto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinAmountEntity>)

    @Query("SELECT * FROM owned_coins")
    fun getAllCoins(): List<CoinAmountEntity>

    @Query("DELETE FROM owned_coins")
    suspend fun clearCoins()
}
