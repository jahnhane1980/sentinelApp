package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.sentinel.deeptrace.ui.dashboard.dialogs.EditStockDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Typ-Inferenz Fix
    var itemToEdit by remember {
        mutableStateOf<WatchlistItem?>(null)
    }

    val data = viewModel.marketData
    val allWatchlistItems by viewModel.watchlist.collectAsState()

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
                    viewModel.addStock(symbol, name)
                    showAddDialog = false
                }
            )
        }

        // FIX FÜR DEN FEHLER IM BILD:
        // Wir nutzen eine einfache if-Abfrage statt .let, das ist in Compose
        // für Dialoge stabiler und vermeidet den "type parameter R" Fehler.
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

        // --- Dashboard Content ---
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
                // Header Score Anzeige
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem("SYSTEM", data.systemScore)
                    StatusHeaderItem("S&P 500", data.sp500Score)
                    StatusHeaderItem("NAS 100", data.nasdaqScore)
                }

                Divider(color = SentinelDivider)

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

                // My Watchlist
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

// --- Hilfskomponenten ---
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