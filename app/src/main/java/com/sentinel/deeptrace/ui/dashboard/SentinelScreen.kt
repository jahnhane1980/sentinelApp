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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType // NEU
import androidx.compose.ui.platform.LocalHapticFeedback // NEU
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
    val watchlist by viewModel.watchlist.collectAsState()

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
                Icon(Icons.Default.Add, contentDescription = "Add Stock")
            }
        }
    ) { innerPadding ->

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem("SYSTEM", data.systemScore)
                    StatusHeaderItem("S&P 500", data.sp500Score)
                    StatusHeaderItem("NAS 100", data.nasdaqScore)
                }

                Divider(color = SentinelDivider)

                Text(
                    "WATCHLIST MONITORING",
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(watchlist) { item ->
                        WatchlistItemComponent(
                            item = item,
                            onDelete = { viewModel.removeStock(item) },
                            onEdit = { itemToEdit = item }
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
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
                                Icon(Icons.Default.Info, contentDescription = null, tint = SentinelBlue)
                                Spacer(Modifier.width(12.dp))
                                Text("MARKET INTELLIGENCE", color = SentinelBlue, fontWeight = FontWeight.Bold)
                            }
                            Text(text = if (isExpanded) "▲" else "▼", color = SentinelBlue)
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(16.dp))
                            DetailRow(label = "VIX (Volatility)", value = data.vix.toString(), color = SentinelOrange)
                            DetailRow(label = "Skew Index", value = data.skew.toString(), color = SentinelBlue)
                            DetailRow(label = "Global Liquidity (M2)", value = data.globalLiquidityM2, color = SentinelTurquoise)
                            DetailRow(label = "Fed Repo Flow", value = data.fedRepoFlow, color = SentinelTurquoise)
                            DetailRow(label = "Truflation (yoy)", value = "${data.truflation}%", color = SentinelBlue)
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Modus: ${if (data.isSimulation) "MARKUP B (Simulation)" else "LIVE MODE"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (data.isSimulation) SentinelOrange else SentinelTurquoise
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchlistItemComponent(
    item: WatchlistItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var showActions by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current // Haptic Feedback Provider holen

    val scoreColor = when {
        item.score >= 7.5 -> SentinelBlue
        item.score >= 4.0 -> SentinelOrange
        else -> SentinelRed
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(SentinelCardSurface, RoundedCornerShape(12.dp))
                .combinedClickable(
                    onClick = { if (showActions) showActions = false },
                    onLongClick = {
                        // VIBRATION AUSLÖSEN
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showActions = true
                    }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.name, fontWeight = FontWeight.Bold, color = SentinelBlue, fontSize = 16.sp)
                if (item.isPermanent) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "System-Hedge",
                        modifier = Modifier.size(14.dp),
                        tint = SentinelBlue.copy(alpha = 0.4f)
                    )
                }
            }
            Surface(color = scoreColor.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = String.format("%.1f", item.score),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = scoreColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        AnimatedVisibility(
            visible = showActions,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row(modifier = Modifier.padding(start = 8.dp)) {
                IconButton(
                    onClick = { onEdit(); showActions = false },
                    modifier = Modifier.background(SentinelBlue, RoundedCornerShape(8.dp)).size(40.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (!item.isPermanent) {
                            onDelete()
                            showActions = false
                        }
                    },
                    modifier = Modifier
                        .background(
                            if (item.isPermanent) Color.Gray.copy(alpha = 0.3f) else SentinelRed,
                            RoundedCornerShape(8.dp)
                        )
                        .size(40.dp),
                    enabled = !item.isPermanent
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                }
            }
        }
    }
}

// ... Restliche Dialoge (EditStockDialog, AddStockDialog) und UI-Komponenten (StatusHeaderItem, DetailRow) bleiben gleich wie zuvor ...

@Composable
fun EditStockDialog(
    item: WatchlistItem,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var symbol by remember { mutableStateOf(item.symbol) }
    var name by remember { mutableStateOf(item.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ticker bearbeiten", color = SentinelBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text("Symbol") },
                    singleLine = true,
                    enabled = !item.isPermanent
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(symbol, name) },
                colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
            ) {
                Text("Aktualisieren")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Abbrechen", color = Color.Gray) }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun AddStockDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ticker hinzufügen", color = SentinelBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text("Symbol (z.B. BTC/USD)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Anzeigename") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(symbol, name) },
                colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Abbrechen", color = Color.Gray) }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    val color = when {
        score >= 7.5 -> SentinelBlue
        score >= 4.0 -> SentinelOrange
        else -> SentinelRed
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = String.format("%.1f", score), fontSize = 26.sp, fontWeight = FontWeight.Black, color = color)
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
    }
}