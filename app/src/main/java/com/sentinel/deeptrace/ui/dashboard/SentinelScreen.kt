package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.deeptrace.ui.dashboard.components.*
import com.sentinel.deeptrace.ui.dashboard.dialogs.*
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.data.model.WatchlistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    // UI States
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Fix für Typ-Inferenz (State für den Edit-Dialog)
    var itemToEdit: WatchlistItem? by remember {
        mutableStateOf(null)
    }

    // Daten aus dem ViewModel
    val data = viewModel.marketData
    val allWatchlistItems by viewModel.watchlist.collectAsState()

    // Filterung der Watchlist
    val systemHedges = allWatchlistItems.filter { it.isPermanent }
    val userWatchlist = allWatchlistItems.filter { !it.isPermanent }

    Scaffold(
        containerColor = SentinelBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "SENTINEL DEEP TRACE",
                        color = SentinelBlue,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 2.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SentinelBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SentinelBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        }
    ) { innerPadding ->

        // --- Dialog-Logik ---
        if (showAddDialog) {
            AddStockDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { symbol, name ->
                    viewModel.addStock(symbol, name)
                    showAddDialog = false
                }
            )
        }

        // Stabiler Check statt .let für den Edit-Dialog
        val currentItem = itemToEdit
        if (currentItem != null) {
            EditStockDialog(
                item = currentItem,
                onDismiss = { itemToEdit = null },
                onConfirm = { newSymbol, newName ->
                    viewModel.updateStock(currentItem, newName, newSymbol)
                    itemToEdit = null
                }
            )
        }

        // --- Haupt-Content ---
        if (data == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SentinelBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                // Score Header (Jetzt aus MarketIntelligenceComponents)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem("SYSTEM", data.systemScore)
                    StatusHeaderItem("S&P 500", data.sp500Score)
                    StatusHeaderItem("NAS 100", data.nasdaqScore)
                }

                HorizontalDivider(color = SentinelDivider)

                // Market Intelligence Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clickable { isExpanded = !isExpanded },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = SentinelBlue)
                                Spacer(Modifier.width(12.dp))
                                Text("MARKET INTELLIGENCE", color = SentinelBlue, fontWeight = FontWeight.Bold)
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SentinelBlue
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(16.dp))
                            systemHedges.forEach { hedge ->
                                SystemIntelligenceItem(hedge)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = SentinelBlue.copy(alpha = 0.1f)
                            )
                            DetailRow("VIX (Volatility)", data.vix.toString(), SentinelOrange)
                            DetailRow("Skew Index", data.skew.toString(), SentinelBlue)
                            DetailRow("Fed Repo Flow", data.fedRepoFlow, SentinelTurquoise)
                        }
                    }
                }

                // User Watchlist
                Text(
                    "MY WATCHLIST",
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(userWatchlist) { item ->
                        WatchlistItemComponent(
                            item = item,
                            onDelete = { viewModel.removeStock(item) },
                            onEdit = { itemToEdit = item }
                        )
                    }
                }

                // Footer
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "SENTINEL LIVE MODE",
                        style = MaterialTheme.typography.bodySmall,
                        color = SentinelTurquoise,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}