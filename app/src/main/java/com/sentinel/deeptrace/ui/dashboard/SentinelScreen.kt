package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.dashboard.components.*
import com.sentinel.deeptrace.ui.dashboard.dialogs.*
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isIntelligenceExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var editingAsset by remember { mutableStateOf<WatchlistWithDetails?>(null) }

    val watchlistItems by viewModel.watchlist.collectAsState()
    val marketData = viewModel.marketData

    Scaffold(
        containerColor = SentinelBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SentinelBlue,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.small
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(SentinelDimens.SpacingMedium)
            ) {
                item { SentinelHeader(watchlist = watchlistItems) }

                item {
                    marketData?.let { data ->
                        MarketIntelligenceCard(
                            data = data,
                            systemHedges = watchlistItems.filter { it.isPermanent },
                            isExpanded = isIntelligenceExpanded,
                            onExpandClick = { isIntelligenceExpanded = !isIntelligenceExpanded }
                        )
                    }
                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))
                }

                items(watchlistItems.filter { !it.isPermanent }) { item ->
                    WatchlistItemComponent(
                        item = item,
                        onDelete = { viewModel.removeStock(item.symbol) },
                        onEdit = { editingAsset = item }
                    )
                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))
                }
            }

            IconButton(
                onClick = { showSettings = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(SentinelDimens.SpacingSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.label_settings),
                    tint = SentinelBlue.copy(alpha = 0.7f)
                )
            }
        }
    }

    if (showAddDialog) {
        AddStockDialog(onDismiss = { showAddDialog = false }, viewModel = viewModel)
    }

    editingAsset?.let { asset ->
        val currencies by viewModel.availableCurrencies.collectAsState()
        EditHoldingsDialog(
            item = asset,
            availableCurrencies = currencies,
            onDismiss = { editingAsset = null },
            onConfirm = { amount, price, currency ->
                viewModel.bookTransaction(asset.symbol, amount, price, currency)
                editingAsset = null
            }
        )
    }

    if (showSettings) {
        SettingsSheet(
            onDismiss = { showSettings = false },
            viewModel = viewModel // FIX: ViewModel wird nun korrekt übergeben
        )
    }
}