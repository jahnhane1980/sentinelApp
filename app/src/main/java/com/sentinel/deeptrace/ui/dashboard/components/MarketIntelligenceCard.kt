package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.config.AppConfig
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun MarketIntelligenceCard(
    data: MarketData,
    systemHedges: List<WatchlistWithDetails>,
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
            // Header: Titel & Expand-Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.market_intelligence),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SentinelBlue
                )
            }

            Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

            // Haupt-Scores (System, S&P 500, Nasdaq)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusHeaderItem(stringResource(R.string.header_system), data.systemScore)
                StatusHeaderItem(stringResource(R.string.header_sp500), data.sp500Score)
                StatusHeaderItem(stringResource(R.string.header_nasdaq), data.nasdaqScore)
            }

            // Erweiterter Bereich (Details & Warnungen)
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

                    // Makro-Kennzahlen
                    DetailRow(stringResource(R.string.label_vix), String.format("%.1f", data.vix))
                    DetailRow(stringResource(R.string.label_fed_repo), "$${data.fedRepoFlow}B")
                    DetailRow(stringResource(R.string.label_liquidity), "$${data.globalLiquidityM2}T")
                    DetailRow(stringResource(R.string.label_truflation), String.format("%.1f%%", data.truflation))

                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                    // Japan/Carry-Trade Risiko-Check (Hypothese)
                    if (data.usdJpy < AppConfig.MarketRisks.USD_JPY_CRITICAL_LEVEL) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = SentinelDimens.SpacingSmall),
                            colors = CardDefaults.cardColors(
                                containerColor = SentinelRed.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(SentinelDimens.SpacingSmall),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = SentinelRed,
                                    modifier = Modifier.size(SentinelDimens.IconSizeSmall)
                                )
                                Spacer(modifier = Modifier.width(SentinelDimens.SpacingSmall))
                                Text(
                                    text = "${stringResource(R.string.exit_indication)}: USD/JPY < ${AppConfig.MarketRisks.USD_JPY_CRITICAL_LEVEL} (Carry-Trade)",
                                    color = SentinelRed,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // System-Hedges (z.B. Gold, BTC aus der permanenten Watchlist)
                    systemHedges.forEach { item ->
                        SystemIntelligenceItem(item)
                        Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))
                    }
                }
            }
        }
    }
}