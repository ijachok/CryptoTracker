package me.ijachok.cryptotracker.crypto.presentation.coin_search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    shape: Shape = RoundedCornerShape(12.dp),
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    placeHolderText: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    leadingSearchBarIcon: @Composable() (() -> Unit)? = null,
    trailingSearchBarIcon: @Composable() (() -> Unit)? = null,
    searchResults: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(leadingIcon != {}){
            AnimatedVisibility(visible = !expanded) {
                leadingIcon()
                Spacer(Modifier.width(8.dp))
            }
        }
        CryptoSearchBar(
            modifier = Modifier.weight(1f),
            query = query,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            shape = shape,
            onSearch = onSearch,
            onQueryChange = onQueryChange,
            placeHolderText = placeHolderText,
            leadingIcon = leadingSearchBarIcon,
            trailingIcon = trailingSearchBarIcon,
            searchResults = searchResults
        )
        if(trailingIcon != {}){
            AnimatedVisibility(visible = !expanded) {
                Spacer(Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    shape: Shape = RoundedCornerShape(12.dp),
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    placeHolderText: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    searchResults: @Composable () -> Unit
) {
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch(it)
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = placeHolderText,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        shape = shape,
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        searchResults()
    }
}

@PreviewLightDark
@Composable
private fun TSBPrev() {
    CryptoTrackerTheme {
        TopSearchBar(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            query = "",
            expanded = false,
            onExpandedChange = {},
            onSearch = {},
            onQueryChange = {},
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            trailingSearchBarIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            }

        ) {
            for (i in 0..4) {
                Row {

                }
            }
        }
    }
}