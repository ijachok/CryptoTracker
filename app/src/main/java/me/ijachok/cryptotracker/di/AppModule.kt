package me.ijachok.cryptotracker.di

import me.ijachok.cryptotracker.crypto.presentation.coin_home.CoinHomeViewModel
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import me.ijachok.cryptotracker.crypto.presentation.coin_search.CoinSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CoinListViewModel)
    viewModelOf(::CoinSearchViewModel)
    viewModelOf(::CoinHomeViewModel)
}