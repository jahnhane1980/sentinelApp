package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun MarketIntelligenceCard(
    data: MarketData,
    systemHedges: List<WatchlistItem>,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandClick() },
        colors = CardDefaults.cardColors(containerColor = SentinelCardBlue),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.market_intelligence),
                    style = MaterialTheme.typography.labelSmall,
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SentinelBlue
                )
            }

            Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusHeaderItem(stringResource(R.string.header_system), data.systemScore)
                StatusHeaderItem(stringResource(R.string.header_sp500), data.sp500Score)
                StatusHeaderItem(stringResource(R.string.header_nasdaq), data.nasdaqScore)
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = SentinelDimens.SpacingMedium),
                        color = SentinelBlue.copy(alpha = 0.1f)
                    )

                    // FIX: Hier werden die Doubles in Strings umgewandelt
                    DetailRow(stringResource(R.string.label_vix), String.format("%.1f", data.vix))
                    DetailRow(stringResource(R.string.label_fed_repo), "$${data.fedRepoFlow}B")
                    DetailRow(stringResource(R.string.label_liquidity), "$${data.globalLiquidityM2}T")
                    DetailRow(stringResource(R.string.label_truflation), String.format("%.1f%%", data.truflation))

                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                    systemHedges.forEach { item ->
                        SystemIntelligenceItem(item)
                        Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))
                    }
                }
            }
        }
    }
}