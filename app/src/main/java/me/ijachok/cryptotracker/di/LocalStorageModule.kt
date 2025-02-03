package me.ijachok.cryptotracker.di

import me.ijachok.cryptotracker.core.data.local.LocalDatabaseFactory
import me.ijachok.cryptotracker.crypto.data.local.LocalCoinDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localStorageModule = module {
    single { LocalDatabaseFactory.create(androidContext()) }  // Ensure this is single
    single { LocalCoinDataSource(get()) }

}