package me.ijachok.cryptotracker.di

import io.ktor.client.engine.cio.CIO
import me.ijachok.cryptotracker.core.data.networking.HttpClientFactory
import me.ijachok.cryptotracker.core.domain.CoinDataSource
import me.ijachok.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

}