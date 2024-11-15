package me.ijachok.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ijachok.cryptotracker.core.navigation.AdaptiveCoinListDetailPane
import me.ijachok.cryptotracker.core.presentation.util.ObserveAsEvents
import me.ijachok.cryptotracker.core.presentation.util.toString
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import me.ijachok.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdaptiveCoinListDetailPane(Modifier.padding(innerPadding))
                }
            }
        }
    }
}