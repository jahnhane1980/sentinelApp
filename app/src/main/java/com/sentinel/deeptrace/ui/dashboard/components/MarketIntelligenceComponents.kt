package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.config.AppConfig // Neu: Import der Config

@Composable
fun StatusHeaderItem(label: String, score: Double) {
    // Logik über AppConfig gesteuert
    val color = when {
        score >= AppConfig.Thresholds.SCORE_HIGH -> SentinelBlue
        score >= AppConfig.Thresholds.SCORE_MEDIUM -> SentinelOrange
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
            color = Color.Gray
        )
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun SystemIntelligenceItem(item: WatchlistItem) {
    // Logik über AppConfig gesteuert
    val color = when {
        item.score >= AppConfig.Thresholds.SCORE_HIGH -> SentinelBlue
        item.score >= AppConfig.Thresholds.SCORE_MEDIUM -> SentinelOrange
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
            fontWeight = FontWeight.Black,
            color = color,
            fontSize = 18.sp
        )
    }
}