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
import com.sentinel.deeptrace.core.SentinelConfig
import kotlinx.coroutines.launch

// Deine Design-Grundbereiche
val SentinelBlue = Color(0xFF3366CC)      // Hauptfarbe / Schrift
val SentinelTurquoise = Color(0xFF40E0D0) // Highlights / Positive Werte
val SentinelOrange = Color(0xFFFF8C00)    // Warnungen / Mock-Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White, // Grundsatz: Immer weißer Hintergrund
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SENTINEL DEEP TRACE",
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- SCORE SECTION ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8FAFF) // Minimaler Kontrast zum Weiß
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MARKET RISK SCORE",
                        style = MaterialTheme.typography.labelSmall,
                        color = SentinelBlue.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = String.format("%.1f", viewModel.currentScore),
                        fontSize = 84.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = getScoreColor(viewModel.currentScore)
                    )

                    // Akzent-Linie in Türkis
                    Surface(
                        modifier = Modifier.width(50.dp).height(4.dp),
                        color = SentinelTurquoise,
                        shape = RoundedCornerShape(2.dp)
                    ) {}
                }
            }

            // --- SYSTEM STATUS SECTION ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(
                    label = "INTERVALL",
                    value = viewModel.frequency.toString(),
                    highlightColor = SentinelBlue
                )

                VerticalDivider(modifier = Modifier.height(30.dp).width(1.dp), color = Color.LightGray)

                InfoItem(
                    label = "SYSTEM",
                    value = if (SentinelConfig.USE_MOCK_DATA) "MOCK" else "LIVE",
                    highlightColor = if (SentinelConfig.USE_MOCK_DATA) SentinelOrange else SentinelTurquoise
                )
            }

            // --- DETAILED METRICS SECTION ---
            Text(
                text = "DETAILED METRICS",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 4.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD)),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Später binden wir diese Werte an das ViewModel
                    MetricRow(label = "USD / JPY", value = "159.3", color = SentinelBlue)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                    MetricRow(label = "VIX INDEX", value = "23.5", color = SentinelBlue)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                    MetricRow(label = "FED REPO FLOW", value = "620B", color = SentinelBlue)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- ACTION BUTTON ---
            Button(
                onClick = {
                    viewModel.updateAnalysis()
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Deep Trace Analysis updated",
                            withDismissAction = true
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SentinelBlue
                )
            ) {
                Text(
                    "RUN ANALYSIS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
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
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = highlightColor
        )
    }
}

@Composable
fun MetricRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, color = color)
        Text(text = value, fontWeight = FontWeight.Bold, color = SentinelTurquoise)
    }
}

fun getScoreColor(score: Double): Color {
    return when {
        score >= 7.5 -> SentinelTurquoise
        score >= 4.0 -> SentinelOrange
        else -> Color(0xFFD32F2F) // Klassisches Rot für Gefahr
    }
}