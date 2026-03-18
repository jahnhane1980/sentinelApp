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
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.ui.theme.SentinelDimens
import com.sentinel.deeptrace.config.AppConfig

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
            style = MaterialTheme.typography.headlineLarge, // Zentral gesteuert
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,    // Zentral gesteuert
            color = Color.Gray
        )
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SentinelDimens.SpacingSmall / 2),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SystemIntelligenceItem(item: WatchlistItem) {
    val color = when {
        item.score >= AppConfig.Thresholds.SCORE_HIGH -> SentinelBlue
        item.score >= AppConfig.Thresholds.SCORE_MEDIUM -> SentinelOrange
        else -> SentinelRed
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(SentinelDimens.SpacingMedium), // Konsistenter Abstand
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = SentinelBlue
            )
            Text(
                text = item.symbol,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(
            text = String.format("%.1f", item.score),
            style = MaterialTheme.typography.titleMedium, // Nutzt Theme statt Hart-Code
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}