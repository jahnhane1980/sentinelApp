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

    // Trennung von System-Analysen und User-Einträgen
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

        // --- Dialog Steuerung ---
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

        // --- UI Content ---
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
                // Cockpit Scores
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem("SYSTEM", data.systemScore)
                    StatusHeaderItem("S&P 500", data.sp500Score)
                    StatusHeaderItem("NAS 100", data.nasdaqScore)
                }

                Divider(color = SentinelDivider)

                // Market Intelligence (System Hedges)
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
                            systemHedges.forEach { hedge ->
                                SystemIntelligenceItem(hedge)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = SentinelBlue.copy(alpha = 0.1f))
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

                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text("SENTINEL LIVE MODE", style = MaterialTheme.typography.bodySmall, color = SentinelTurquoise, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SystemIntelligenceItem(item: WatchlistItem) {
    val color = if (item.score >= 7.5) SentinelBlue else if (item.score >= 4.0) SentinelOrange else SentinelRed
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = SentinelBlue)
            Text(item.symbol, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Text(String.format("%.1f", item.score), fontWeight = FontWeight.Black, color = color, fontSize = 18.sp)
    }
}

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
        AnimatedVisibility(visible = showActions, enter = expandHorizontally(), exit = shrinkHorizontally()) {
            Row(modifier = Modifier.padding(start = 8.dp)) {
                IconButton(onClick = { onEdit(); showActions = false }, modifier = Modifier.background(SentinelBlue, RoundedCornerShape(12.dp)).size(40.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { onDelete(); showActions = false }, modifier = Modifier.background(SentinelRed, RoundedCornerShape(12.dp)).size(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddStockDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neuer Watchlist-Ticker", color = SentinelBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol, onValueChange = { symbol = it.uppercase() }, label = { Text("Symbol") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, label = { Text("Anzeigename") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(symbol, name) },
                modifier = Modifier.height(52.dp).fillMaxWidth(0.4f), // Einheitliche Höhe für Kantigkeit
                colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Hinzufügen", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.height(52.dp)) {
                Text("Abbrechen", color = Color.Gray)
            }
        },
        containerColor = Color.White, shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun EditStockDialog(item: WatchlistItem, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var symbol by remember { mutableStateOf(item.symbol) }
    var name by remember { mutableStateOf(item.name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ticker bearbeiten", color = SentinelBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol, onValueChange = { symbol = it.uppercase() }, label = { Text("Symbol") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, label = { Text("Anzeigename") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(symbol, name) },
                modifier = Modifier.height(52.dp).fillMaxWidth(0.4f),
                colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Speichern", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.height(52.dp)) {
                Text("Abbrechen", color = Color.Gray)
            }
        },
        containerColor = Color.White, shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    val color = if (score >= 7.5) SentinelBlue else if (score >= 4.0) SentinelOrange else SentinelRed
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(String.format("%.1f", score), fontSize = 26.sp, fontWeight = FontWeight.Black, color = color)
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = color)
    }
}