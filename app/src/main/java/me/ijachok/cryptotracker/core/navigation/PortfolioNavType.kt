package me.ijachok.cryptotracker.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio

val PortfolioNavType = object : NavType<List<CoinPortfolio>>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): List<CoinPortfolio>? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): List<CoinPortfolio> {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: List<CoinPortfolio>): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: List<CoinPortfolio>) {
        bundle.putString(key, Json.encodeToString(value))
    }

}