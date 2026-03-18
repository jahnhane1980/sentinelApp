package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.dashboard.components.*
import com.sentinel.deeptrace.ui.dashboard.dialogs.*
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAsset by remember { mutableStateOf<WatchlistWithDetails?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val watchlistItems by viewModel.watchlist.collectAsState()
    val marketData = viewModel.marketData

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = SentinelBackground,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = SentinelBlue) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(horizontal = SentinelDimens.ScreenPadding)) {
            // Header & Market Intelligence
            marketData?.let {
                MarketIntelligenceCard(it, watchlistItems.filter { it.isPermanent }, isExpanded) { isExpanded = !isExpanded }
            }

            Spacer(modifier = Modifier.height(SentinelDimens.SpacingExtraLarge))

            // Watchlist
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SentinelDimens.ListSpacing)) {
                items(watchlistItems.filter { !it.isPermanent }, key = { it.symbol }) { item ->
                    WatchlistItemComponent(
                        item = item,
                        onDelete = { viewModel.removeStock(item.symbol) },
                        onEdit = { editingAsset = item }
                    )
                }
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
            onConfirm = { a, p, c ->
                viewModel.bookTransaction(asset.symbol, a, p, c)
                editingAsset = null
            }
        )
    }
}