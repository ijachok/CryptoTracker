package me.ijachok.cryptotracker.di

import io.ktor.client.engine.cio.CIO
import me.ijachok.cryptotracker.core.data.local.LocalDatabase
import me.ijachok.cryptotracker.core.data.local.LocalDatabaseFactory
import me.ijachok.cryptotracker.core.data.networking.HttpClientFactory
import me.ijachok.cryptotracker.core.domain.CoinDataSource
import me.ijachok.cryptotracker.crypto.data.local.LocalCoinDataSource
import me.ijachok.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import me.ijachok.cryptotracker.crypto.presentation.coin_home.CoinHomeScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_home.CoinHomeViewModel
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import me.ijachok.cryptotracker.crypto.presentation.coin_search.CoinSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single { LocalDatabaseFactory.create(androidContext()) }  // Ensure this is single
    single { LocalCoinDataSource(get()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)
    viewModelOf(::CoinSearchViewModel)
    viewModelOf(::CoinHomeViewModel)
}