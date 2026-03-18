package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.data.model.WatchlistItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<WatchlistItem?>(null) }

    val data = viewModel.marketData
    val allWatchlistItems by viewModel.watchlist.collectAsState()

    // FILTERUNG: System-Hedges für Market Intelligence vs. User-Watchlist
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SentinelBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SentinelBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Stock")
            }
        }
    ) { innerPadding ->

        // Dialoge
        if (showAddDialog) {
            AddStockDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { symbol, name ->
                    if (symbol.isNotBlank()) viewModel.addStock(symbol, name)
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
                // Top Scores
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem("SYSTEM", data.systemScore)
                    StatusHeaderItem("S&P 500", data.sp500Score)
                    StatusHeaderItem("NAS 100", data.nasdaqScore)
                }

                Divider(color = SentinelDivider)

                // ABSCHNITT 1: MARKET INTELLIGENCE (Die fixen Hedges)
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
                            Text(text = if (isExpanded) "▲" else "▼", color = SentinelBlue)
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Anzeige der System-Hedges aus der DB
                            systemHedges.forEach { hedge ->
                                SystemIntelligenceItem(hedge)
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = SentinelBlue.copy(alpha = 0.1f))

                            // Makro-Daten
                            DetailRow(label = "VIX (Volatility)", value = data.vix.toString(), color = SentinelOrange)
                            DetailRow(label = "Skew Index", value = data.skew.toString(), color = SentinelBlue)
                            DetailRow(label = "Global Liquidity (M2)", value = data.globalLiquidityM2, color = SentinelTurquoise)
                        }
                    }
                }

                // ABSCHNITT 2: USER WATCHLIST
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

                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "SENTINEL LIVE MODE",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = SentinelTurquoise
                    )
                }
            }
        }
    }
}

@Composable
fun SystemIntelligenceItem(item: WatchlistItem) {
    val color = when {
        item.score >= 7.5 -> SentinelBlue
        item.score >= 4.0 -> SentinelOrange
        else -> SentinelRed
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SentinelBlue)
            Text(item.symbol, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Text(
            text = String.format("%.1f", item.score),
            fontWeight = FontWeight.Black,
            color = color,
            fontSize = 18.sp
        )
    }
}

// ... WatchlistItemComponent (identisch zu vorher, nur für User-Watchlist) ...
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchlistItemComponent(item: WatchlistItem, onDelete: () -> Unit, onEdit: () -> Unit) {
    var showActions by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val scoreColor = if (item.score >= 7.5) SentinelBlue else if (item.score >= 4.0) SentinelOrange else SentinelRed

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.weight(1f).background(SentinelCardSurface, RoundedCornerShape(12.dp))
                .combinedClickable(
                    onClick = { if (showActions) showActions = false },
                    onLongClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); showActions = true }
                ).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.name, fontWeight = FontWeight.Bold, color = SentinelBlue)
            Surface(color = scoreColor.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                Text(String.format("%.1f", item.score), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = scoreColor, fontWeight = FontWeight.Bold)
            }
        }
        AnimatedVisibility(visible = showActions) {
            Row(modifier = Modifier.padding(start = 8.dp)) {
                IconButton(onClick = { onEdit(); showActions = false }, modifier = Modifier.background(SentinelBlue, RoundedCornerShape(8.dp)).size(40.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { onDelete(); showActions = false }, modifier = Modifier.background(SentinelRed, RoundedCornerShape(8.dp)).size(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

// ... Dialoge und HeaderItems (AddStockDialog, EditStockDialog, StatusHeaderItem, DetailRow) bleiben gleich ...
@Composable
fun AddStockDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Neuer Watchlist-Ticker") },
        text = { Column { OutlinedTextField(value = symbol, onValueChange = { symbol = it.uppercase() }, label = { Text("Symbol") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }) } },
        confirmButton = { Button(onClick = { onConfirm(symbol, name) }) { Text("Hinzufügen") } })
}

@Composable
fun EditStockDialog(item: WatchlistItem, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var symbol by remember { mutableStateOf(item.symbol) }
    var name by remember { mutableStateOf(item.name) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Ticker bearbeiten") },
        text = { Column { OutlinedTextField(value = symbol, onValueChange = { symbol = it.uppercase() }, label = { Text("Symbol") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }) } },
        confirmButton = { Button(onClick = { onConfirm(symbol, name) }) { Text("Speichern") } })
}

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    val color = if (score >= 7.5) SentinelBlue else if (score >= 4.0) SentinelOrange else SentinelRed
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(String.format("%.1f", score), fontSize = 26.sp, fontWeight = FontWeight.Black, color = color)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = color)
    }
}