package me.ijachok.cryptotracker.di

import io.ktor.client.engine.cio.CIO
import me.ijachok.cryptotracker.core.data.networking.HttpClientFactory
import me.ijachok.cryptotracker.core.domain.CoinDataSource
import me.ijachok.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)
}