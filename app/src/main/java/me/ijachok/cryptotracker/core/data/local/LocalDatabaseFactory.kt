package me.ijachok.cryptotracker.core.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room

object LocalDatabaseFactory {
    fun create(context:Context):LocalDatabase{
        return Room.databaseBuilder(
            context =context,
            klass =LocalDatabase::class.java,
            name = "owned_coins"
        ).fallbackToDestructiveMigration().build()
    }
}