package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.config.AppConfig
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun SentinelHeader(
    watchlist: List<WatchlistWithDetails>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SentinelDimens.HeaderVerticalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fokus nur noch auf das Kapital
        Text(
            text = stringResource(R.string.header_total_invested),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))

        // Berechnung der Summen pro Währung
        val totals = watchlist
            .filter { it.totalInvested > 0 }
            .groupBy { it.currency ?: AppConfig.UserPreferences.DEFAULT_CURRENCY }
            .mapValues { entry -> entry.value.sumOf { it.totalInvested } }

        if (totals.isEmpty()) {
            Text(
                text = stringResource(R.string.default_empty_balance, AppConfig.UserPreferences.DEFAULT_CURRENCY),
                style = MaterialTheme.typography.headlineMedium,
                color = SentinelBlue, // Geändert auf Standard-Dunkelblau
                fontWeight = FontWeight.Bold
            )
        } else {
            totals.forEach { (currency, sum) ->
                Text(
                    text = String.format("%,.2f %s", sum, currency),
                    style = MaterialTheme.typography.headlineMedium,
                    color = SentinelBlue, // Geändert auf Standard-Dunkelblau
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}