package me.ijachok.cryptotracker.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.home, Icons.Rounded.Home, R.string.home),
    PORTFOLIO(R.string.portfolio, Icons.AutoMirrored.Rounded.List, R.string.portfolio),
    GLOBAL(R.string.global, Icons.Rounded.Public, R.string.global),
    SEARCH(R.string.search, Icons.Rounded.Search, R.string.search)
}

@Serializable
object HomeDestination

@Serializable
object PortfolioDestination