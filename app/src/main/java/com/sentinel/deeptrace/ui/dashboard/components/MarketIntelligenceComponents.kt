package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.config.AppConfig

/**
 * Kleine Komponente für die Kopfzeile (System, S&P 500, Nasdaq)
 * Nutzt die Schwellenwerte aus der AppConfig für die farbliche Kennzeichnung.
 */
@Composable
fun StatusHeaderItem(label: String, score: Double) {
    val color = when {
        score >= AppConfig.Thresholds.SCORE_HIGH -> SentinelBlue
        score >= AppConfig.Thresholds.SCORE_MEDIUM -> SentinelOrange
        else -> SentinelRed
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = String.format("%.1f", score),
            style = MaterialTheme.typography.headlineLarge,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}


// In MarketIntelligenceComponents.kt ergänzen/behalten:
/**
 * ZENTRALE DEFINITION: Nur hier darf DetailRow stehen!
 */
@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SentinelDimens.SpacingExtraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Die permanenten Items (Gold, Yen, SPX) innerhalb der MarketIntelligenceCard.
 * Zeigt den Namen aus dem AssetMaster und den Marktnamen an.
 */
@Composable
fun SystemIntelligenceItem(item: WatchlistWithDetails) {
    val color = when {
        item.score >= AppConfig.Thresholds.SCORE_HIGH -> SentinelBlue
        item.score >= AppConfig.Thresholds.SCORE_MEDIUM -> SentinelOrange
        else -> SentinelRed
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.7f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(SentinelDimens.SpacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = SentinelBlue
            )
            Text(
                text = "${item.symbol} | ${item.marketName}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(
            text = String.format("%.1f", item.score),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}