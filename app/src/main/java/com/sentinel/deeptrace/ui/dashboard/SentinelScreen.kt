package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Hier bekommen wir MarketData? (Optional)
    val marketData = viewModel.marketData

    // Hier bekommen wir List<WatchlistWithDetails>
    val watchlistItems by viewModel.watchlist.collectAsState()

    val userWatchlist = watchlistItems.filter { !it.isPermanent }
    val systemHedges = watchlistItems.filter { it.isPermanent }

    LaunchedEffect(viewModel.uiErrorMessage) {
        viewModel.uiErrorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                .padding(padding)
                .padding(horizontal = SentinelDimens.ScreenPadding)
        ) {
            Text(
                text = "SENTINEL DEEP TRACE",
                style = MaterialTheme.typography.labelLarge,
                color = SentinelBlue,
                modifier = Modifier.padding(vertical = SentinelDimens.HeaderVerticalPadding)
            )

            // FIX: data?.let stellt sicher, dass 'it' nicht null ist
            marketData?.let { data ->
                MarketIntelligenceCard(
                    data = data,
                    systemHedges = systemHedges,
                    isExpanded = isExpanded,
                    onExpandClick = { isExpanded = !isExpanded }
                )
            } ?: run {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = SentinelBlue,
                    trackColor = SentinelCardBlue
                )
            }

            Spacer(modifier = Modifier.height(SentinelDimens.SpacingExtraLarge))

            Text(
                text = stringResource(R.string.my_watchlist),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = SentinelDimens.SpacingSmall)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SentinelDimens.ListSpacing)
            ) {
                items(userWatchlist) { item ->
                    WatchlistItemComponent(
                        item = item,
                        onDelete = { viewModel.removeStock(item.symbol) },
                        onEdit = { /* Deaktiviert */ }
                    )
                }
            }

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

    if (showAddDialog) {
        AddStockDialog(onDismiss = { showAddDialog = false }, viewModel = viewModel)
    }
}