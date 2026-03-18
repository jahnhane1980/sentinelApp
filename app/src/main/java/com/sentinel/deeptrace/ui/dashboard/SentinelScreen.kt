package com.sentinel.deeptrace.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.dashboard.components.MarketIntelligenceCard
import com.sentinel.deeptrace.ui.dashboard.components.WatchlistItemComponent
import com.sentinel.deeptrace.ui.dashboard.dialogs.AddStockDialog
import com.sentinel.deeptrace.ui.dashboard.dialogs.EditHoldingsDialog
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
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {
                item {
                    marketData?.let {
                        MarketIntelligenceCard(
                            data = it,
                            systemHedges = watchlistItems.filter { asset -> asset.score >= 8.0 },
                            isExpanded = isIntelligenceExpanded,
                            onExpandClick = { isIntelligenceExpanded = !isIntelligenceExpanded }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                items(watchlistItems) { watchlistItem ->
                    WatchlistItemComponent(
                        item = watchlistItem,
                        onDelete = { viewModel.removeStock(watchlistItem.symbol) },
                        onEdit = { editingAsset = watchlistItem }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            IconButton(
                onClick = { showSettings = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Settings, null, tint = SentinelBlue.copy(alpha = 0.7f))
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
        SettingsSheet(onDismiss = { showSettings = false }, viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(onDismiss: () -> Unit, viewModel: SentinelViewModel) {
    val context = LocalContext.current
    var settingsChanged by remember { mutableStateOf(false) }

    val handleDismiss = {
        if (settingsChanged) {
            Toast.makeText(context, "Einstellungen wurden aktualisiert", Toast.LENGTH_SHORT).show()
        }
        onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = handleDismiss,
        containerColor = SentinelBackground,
        dragHandle = { BottomSheetDefaults.DragHandle(color = SentinelBlue) }
    ) {
        Box(modifier = Modifier.fillMaxWidth().navigationBarsPadding().imePadding()) {
            SettingsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(bottom = 32.dp),
                onSettingInteracted = { settingsChanged = true }
            )
        }
    }
}