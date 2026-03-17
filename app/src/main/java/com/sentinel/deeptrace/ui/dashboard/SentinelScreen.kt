package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Deine definierten Farben
val SentinelBlue = Color(0xFF3366CC)
val SentinelTurquoise = Color(0xFF40E0D0)
val SentinelOrange = Color(0xFFFF8C00)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White, // Immer weißer Hintergrund
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("SENTINEL DEEP TRACE",
                        color = SentinelBlue,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 2.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Die Score-Karte mit Türkis-Highlight
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F8FF) // Ganz leichtes Blau-Weiß für Tiefe
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 40.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MARKET RISK SCORE",
                        style = MaterialTheme.typography.labelSmall,
                        color = SentinelBlue.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = String.format("%.1f", viewModel.currentScore),
                        fontSize = 84.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = getScoreColor(viewModel.currentScore)
                    )

                    // Ein kleiner türkiser Akzentstrich unter dem Score
                    Divider(
                        modifier = Modifier.width(40.dp).padding(top = 8.dp),
                        thickness = 4.dp,
                        color = SentinelTurquoise
                    )
                }
            }

            // Info-Bereich mit deinen neuen Farben
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(label = "INTERVALL", value = viewModel.frequency.toString(), highlightColor = SentinelBlue)
                InfoItem(label = "SYSTEM", value = "ACTIVE", highlightColor = SentinelTurquoise)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Der Button nutzt jetzt das Sentinel-Blau
            Button(
                onClick = {
                    viewModel.updateAnalysis()
                    scope.launch {
                        snackbarHostState.showSnackbar("Deep Trace Analysis initiated...")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SentinelBlue
                )
            ) {
                Text(
                    "RUN ANALYSIS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, highlightColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = highlightColor
        )
    }
}

fun getScoreColor(score: Double): Color {
    return when {
        score >= 7.5 -> SentinelTurquoise // Sicher / Positiv
        score >= 4.0 -> SentinelOrange    // Warnung / Instabilität
        else -> Color(0xFFD32F2F)         // Kritisch (Standard Rot für Gefahr)
    }
}