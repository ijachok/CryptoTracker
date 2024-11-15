package me.ijachok.cryptotracker.core.data.networking

import me.ijachok.cryptotracker.BuildConfig

fun constructURL(url:String):String{
    return when{
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url
    }
}