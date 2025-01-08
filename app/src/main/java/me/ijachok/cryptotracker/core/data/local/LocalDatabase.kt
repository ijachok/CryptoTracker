package me.ijachok.cryptotracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.ijachok.cryptotracker.crypto.data.local.CoinDao
import me.ijachok.cryptotracker.crypto.data.local.CoinAmountEntity

@Database(entities = [CoinAmountEntity::class], version = 1)
abstract class LocalDatabase:RoomDatabase() {
    abstract fun coinDao(): CoinDao
}