package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.background
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
// WICHTIG: Deine Farb-Konstanten müssen hier importiert werden
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    Scaffold(
        containerColor = SentinelBackground, // Weißer Hintergrund
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
            // 1. TOP STATUS COCKPIT (System, S&P 500, Nasdaq)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Später greifen wir hier auf die Live-Scores des ViewModels zu
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

            // Beispiel-Daten (später aus dem Repository)
            val watchlist = listOf("Apple", "Microsoft", "Tesla", "Nvidia", "Amazon")

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(watchlist) { stock ->
                    WatchlistItem(name = stock, score = (1..10).random().toDouble())
                }
            }

            // 3. RESEARCH BUTTON
            Button(
                onClick = { /* Navigation zu Research Details */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF0F4FF) // Sehr helles Blau als Button-Fläche
                )
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = SentinelBlue)
                Spacer(Modifier.width(8.dp))
                Text(
                    "RESEARCH & BACKGROUND DATA",
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            // 4. MODUS ANZEIGE (Ganz unten)
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
                        color = SentinelOrange // Orange signalisiert Simulation
                    )
                }
            }
        }
    }
}

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    // Farblogik basierend auf deinen Vorgaben
    val color = when {
        score >= 7.5 -> SentinelBlue   // Neutral / Gut
        score >= 4.0 -> SentinelOrange // Bedenken
        else -> SentinelRed           // Krisenmodus
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
            color = color.copy(alpha = 0.08f), // Dezenter Farbhintergrund für den Score
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