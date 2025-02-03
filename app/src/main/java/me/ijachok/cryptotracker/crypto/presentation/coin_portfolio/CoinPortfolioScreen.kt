package me.ijachok.cryptotracker.crypto.presentation.coin_portfolio

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import me.ijachok.cryptotracker.R
import me.ijachok.cryptotracker.core.presentation.util.ObserveAsEvents
import me.ijachok.cryptotracker.crypto.domain.Coin
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolio
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioEvent
import me.ijachok.cryptotracker.crypto.domain.CoinPortfolioUi
import me.ijachok.cryptotracker.crypto.domain.toCoinPortfolioUI
import me.ijachok.cryptotracker.crypto.presentation.coin_home.coinPortfolioListPrev
import me.ijachok.cryptotracker.crypto.presentation.coin_home.components.TopNavigationBar
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.components.AddCoinDialog
import me.ijachok.cryptotracker.crypto.presentation.coin_portfolio.components.CoinPortfolioListItem
import me.ijachok.cryptotracker.crypto.presentation.components.SimpleDialog
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinPortfolioScreen(
    modifier: Modifier = Modifier,
    state: CoinPortfolioState,
    coinPortfolioEvents: Flow<CoinPortfolioEvent>,
    coinHintList: List<Coin>,
    onBackClick: () -> Unit,
    onAction: (CoinPortfolioAction) -> Unit
) {
    val context = LocalContext.current

    var showAddCoinDialog by remember { mutableStateOf(false) }

    var showCoinExistsDialog by remember { mutableStateOf(false) }
    var existingCoinPortfolio by remember { mutableStateOf<CoinPortfolio?>(null) }

    var showEditCoinDialog by remember { mutableStateOf(false) }
    var coinPortfolioToEdit by remember { mutableStateOf<CoinPortfolioUi?>(null) }

    var showDeleteCoinDialog by remember { mutableStateOf(false) }
    var coinPortfolioToDelete by remember { mutableStateOf<CoinPortfolioUi?>(null) }

    ObserveAsEvents(coinPortfolioEvents) { event ->
        when (event) {
            is CoinPortfolioEvent.CoinAlreadyInList -> {
                existingCoinPortfolio = event.coinPortfolio
                showCoinExistsDialog = true
            }

            CoinPortfolioEvent.CoinDoesntExist -> Toast.makeText(
                context,
                context.getString(R.string.coin_doesnt_exist),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    BackHandler {
        if (state.inEditMode)
            onAction(CoinPortfolioAction.OnPortfolioFinishEdit)
        else onBackClick()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = state.inEditMode,
            transitionSpec = {
                // Define enter/exit animations
                (fadeIn(tween(300)) + slideInVertically { -it })
                    .togetherWith(fadeOut(tween(300)) + slideOutVertically { it })
            },
            label = "AnimatedTopAppBar"
        ) { editMode ->
            if (editMode) {
                TopNavigationBar(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    title = stringResource(R.string.editing),
                    leadingIcon = {
                        IconButton(
                            onClick = { onAction(CoinPortfolioAction.OnPortfolioFinishEdit) }
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { showAddCoinDialog = true }
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    }
                )
            } else {
                TopNavigationBar(
                    modifier = Modifier,
                    title = stringResource(R.string.portfolio),
                    leadingIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { onAction(CoinPortfolioAction.OnPortfolioEdit) }
                        ) {
                            Icon(
                                Icons.Rounded.Edit,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }

        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.portfolio, key = { key -> key.id }) { coinUi ->
                CoinPortfolioListItem(
                    modifier = Modifier.fillMaxWidth(),
                    coinPortfolioUi = coinUi,
                    inEditMode = state.inEditMode,
                    onCoinClick = {
                        if (!state.inEditMode ) onAction(
                            CoinPortfolioAction.OnCoinClick(
                                coinUi
                            )
                        )
                    },
                    onCoinDelete = {
                        coinPortfolioToDelete = coinUi
                        showDeleteCoinDialog = true
                    },
                    onCoinEdit = {
                        coinPortfolioToEdit = coinUi
                        showEditCoinDialog = true
                    },
                )
            }
        }

        if (showAddCoinDialog) {
            AddCoinDialog(
                modifier = Modifier,
                coinHintList = coinHintList,
                onConfirm = {
                    onAction(CoinPortfolioAction.OnCoinAdd(it))
                    showAddCoinDialog = false
                },
                onCancel = { showAddCoinDialog = false },
                searchCoin = {
                    onAction(CoinPortfolioAction.OnCoinSearchHitList(it))
                }
            )
        }

        if (showCoinExistsDialog && existingCoinPortfolio != null) {
            SimpleDialog(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.coin_alredy_exists),
                subtitle = stringResource(R.string.replace_coin_amount, existingCoinPortfolio!!.id),
                confirmButtonText = stringResource(R.string.replace),
                cancelButtonText = stringResource(R.string.cancel),
                onConfirm = {
                    showCoinExistsDialog = false
                    onAction(CoinPortfolioAction.OnCoinEdit(existingCoinPortfolio!!.toCoinPortfolioUI()))
                    existingCoinPortfolio = null
                },
                onCancel = {
                    showCoinExistsDialog = false
                    existingCoinPortfolio = null
                }
            )
        }

        if (showDeleteCoinDialog && coinPortfolioToDelete != null) {
            SimpleDialog(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.delete_coin),
                subtitle = stringResource(
                    R.string.delete_coin_from_portfolio,
                    coinPortfolioToDelete!!.name
                ),
                confirmButtonText = stringResource(R.string.delete),
                cancelButtonText = stringResource(R.string.cancel),
                onConfirm = {
                    showDeleteCoinDialog = false
                    onAction(CoinPortfolioAction.OnCoinDelete(coinPortfolioToDelete!!.id))
                    coinPortfolioToDelete = null
                },
                onCancel = {
                    showDeleteCoinDialog = false
                    coinPortfolioToDelete = null
                }
            )
        }

    }
}

@PreviewLightDark
@Composable
private fun CPPrev() {
    CryptoTrackerTheme {
        CoinPortfolioScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            state = CoinPortfolioState(
                portfolio = coinPortfolioListPrev.map { it.toCoinPortfolioUI() }
            ),
            coinPortfolioEvents = emptyFlow(),
            coinHintList = listOf(),
            onAction = {},
            onBackClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun CPPrevEditMode() {
    CryptoTrackerTheme {
        CoinPortfolioScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            state = CoinPortfolioState(
                inEditMode = true,
                portfolio = coinPortfolioListPrev.map { it.toCoinPortfolioUI() }
            ),
            coinPortfolioEvents = emptyFlow(),
            coinHintList = listOf(),
            onAction = {},
            onBackClick = {}
        )
    }
}