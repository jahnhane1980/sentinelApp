package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// WICHTIG: Deine Farb-Konstanten aus der Color.kt
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    // Zustand für das aufklappbare Panel
    var isExpanded by remember { mutableStateOf(false) }

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // 1. TOP STATUS COCKPIT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusHeaderItem("SYSTEM", 4.5)
                StatusHeaderItem("S&P 500", 8.2)
                StatusHeaderItem("NAS 100", 2.1)
            }

            Divider(color = SentinelDivider)

            // 2. WATCHLIST SECTION
            Text(
                "WATCHLIST MONITORING",
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                letterSpacing = 1.sp
            )

            val watchlist = listOf("Apple", "Microsoft", "Tesla", "Nvidia", "Amazon")

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(watchlist) { stock ->
                    WatchlistItem(name = stock, score = (1..10).random().toDouble())
                }
            }

            // 3. EXPANDABLE RESEARCH SECTION (Market Intelligence)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable { isExpanded = !isExpanded }, // Klick macht das Panel auf/zu
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F4FF)
                )
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
                            Text(
                                "MARKET INTELLIGENCE",
                                color = SentinelBlue,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Text(
                            text = if (isExpanded) "▲" else "▼",
                            color = SentinelBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Sichtbarkeit der Details basierend auf isExpanded
                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = SentinelBlue.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Makro-Werte (Später aus dem ViewModel)
                        DetailRow(label = "VIX (Volatility)", value = "23.5", color = SentinelOrange)
                        DetailRow(label = "Skew Index", value = "145.2", color = SentinelBlue)
                        DetailRow(label = "Global Liquidity (M2)", value = "$94.2T", color = SentinelTurquoise)
                        DetailRow(label = "Fed Repo Flow", value = "$620B", color = SentinelTurquoise)
                        DetailRow(label = "Truflation (yoy)", value = "2.8%", color = SentinelBlue)
                    }
                }
            }

            // 4. MODUS ANZEIGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Modus: ", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        text = "MARKUP B (Simulation)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = SentinelOrange
                    )
                }
            }
        }
    }
}

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    val color = when {
        score >= 7.5 -> SentinelBlue
        score >= 4.0 -> SentinelOrange
        else -> SentinelRed
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = String.format("%.1f", score),
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun WatchlistItem(name: String, score: Double) {
    val color = when {
        score >= 7.5 -> SentinelBlue
        score >= 4.0 -> SentinelOrange
        else -> SentinelRed
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SentinelCardSurface, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, fontWeight = FontWeight.Bold, color = SentinelBlue, fontSize = 16.sp)

        Surface(
            color = color.copy(alpha = 0.08f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = String.format("%.1f", score),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}