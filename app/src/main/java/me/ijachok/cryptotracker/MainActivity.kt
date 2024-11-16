package me.ijachok.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.ijachok.cryptotracker.core.navigation.AdaptiveCoinListDetailPane
import me.ijachok.cryptotracker.core.navigation.AdaptiveCoinSearchDetailPane
import me.ijachok.cryptotracker.core.navigation.AppDestinations
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                val appDestinations = listOf(AppDestinations.GLOBAL, AppDestinations.SEARCH)
                var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.GLOBAL) }

                NavigationSuiteScaffold(
                    navigationSuiteItems = {
                        appDestinations.forEach { appDestination ->
                            item(
                                icon = {
                                    Icon(
                                        imageVector = appDestination.icon,
                                        contentDescription = stringResource(appDestination.contentDescription)
                                    )
                                },
                                label = { Text(stringResource(appDestination.contentDescription)) },
                                selected = appDestination == currentDestination,
                                onClick = { currentDestination = appDestination }

                            )
                        }
                    }
                ) {
                    when (currentDestination) {
                        AppDestinations.GLOBAL -> AdaptiveCoinListDetailPane(
                            modifier = Modifier.windowInsetsPadding(
                                WindowInsets.systemBars
                            )
                        )

                        AppDestinations.SEARCH -> AdaptiveCoinSearchDetailPane(
                            Modifier.windowInsetsPadding(
                                WindowInsets.systemBars
                            )
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}