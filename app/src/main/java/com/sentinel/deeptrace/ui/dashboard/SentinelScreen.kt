package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.ui.dashboard.components.*
import com.sentinel.deeptrace.ui.dashboard.dialogs.*
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.data.model.WatchlistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit: WatchlistItem? by remember { mutableStateOf(null) }

    val data = viewModel.marketData
    val allWatchlistItems by viewModel.watchlist.collectAsState()

    // Filtert die permanenten System-Hedges (Gold, Yen, SPX) heraus
    val systemHedges = allWatchlistItems.filter { it.isPermanent }
    // Der Rest ist die normale User-Watchlist
    val userWatchlist = allWatchlistItems.filter { !it.isPermanent }

    Scaffold(
        containerColor = SentinelBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SentinelBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.desc_add_asset))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = SentinelDimens.ScreenPadding)
        ) {
            // Header Logo / App Name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SentinelDimens.HeaderVerticalPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "SENTINEL DEEP TRACE",
                    style = MaterialTheme.typography.labelLarge,
                    color = SentinelBlue,
                    modifier = Modifier.padding(top = SentinelDimens.SpacingSmall)
                )
            }

            // Markt-Intelligenz Box (Aufklappbar)
            data?.let { marketData ->
                MarketIntelligenceCard(
                    data = marketData,
                    systemHedges = systemHedges, // Hedges werden hier hineingereicht
                    isExpanded = isExpanded,
                    onExpandClick = { isExpanded = !isExpanded }
                )
            }

            // Watchlist Bereich
            Text(
                text = stringResource(R.string.my_watchlist),
                modifier = Modifier.padding(
                    top = SentinelDimens.SpacingExtraLarge,
                    bottom = SentinelDimens.SpacingSmall
                ),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SentinelDimens.ListSpacing)
            ) {
                items(userWatchlist) { item ->
                    WatchlistItemComponent(
                        item = item,
                        onDelete = { viewModel.removeStock(item) },
                        onEdit = { itemToEdit = item }
                    )
                }
            }

            // Footer / Live Modus Indikator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SentinelDimens.HeaderVerticalPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.live_mode),
                    style = MaterialTheme.typography.labelLarge,
                    color = SentinelTurquoise
                )
            }
        }
    }

    // Dialoge
    if (showAddDialog) {
        AddStockDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { symbol, name ->
                viewModel.addStock(symbol, name)
                showAddDialog = false
            }
        )
    }

    itemToEdit?.let { item ->
        EditStockDialog(
            item = item,
            onDismiss = { itemToEdit = null },
            onConfirm = { newSymbol, newName ->
                viewModel.updateStock(item, newName, newSymbol)
                itemToEdit = null
            }
        )
    }
}