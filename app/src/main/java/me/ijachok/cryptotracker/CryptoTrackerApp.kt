package me.ijachok.cryptotracker

import android.app.Application
import me.ijachok.cryptotracker.di.appModule
import me.ijachok.cryptotracker.di.localStorageModule
import me.ijachok.cryptotracker.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CryptoTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoTrackerApp)
            androidLogger()

            modules(appModule, networkModule, localStorageModule)
        }
    }
}