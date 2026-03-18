package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchlistItemComponent(
    item: WatchlistWithDetails,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    // State für das Einblenden der Aktions-Buttons
    var showActions by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { showActions = false }, // Schließen bei einfachem Klick
                onLongClick = { showActions = !showActions } // Toggle bei langem Klick
            ),
        colors = CardDefaults.cardColors(containerColor = SentinelCardBlue),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(SentinelDimens.CardPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = SentinelBlue
                )

                // Portfolio-Werte anzeigen (Bestand & Investiert)
                if (item.totalHoldings > 0.0) {
                    Text(
                        text = stringResource(R.string.label_holdings_current, item.totalHoldings, item.currency ?: ""),
                        style = MaterialTheme.typography.labelSmall,
                        color = SentinelTurquoise
                    )
                    Text(
                        text = stringResource(R.string.label_invested, item.totalInvested, item.currency ?: ""),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "${item.symbol} | ${item.marketName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }

            // Animiertes Einfahren der Buttons
            AnimatedVisibility(
                visible = showActions,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Row {
                    IconButton(onClick = {
                        showActions = false
                        onEdit()
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = SentinelBlue)
                    }
                    IconButton(onClick = {
                        showActions = false
                        onDelete()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = SentinelRed)
                    }
                }
            }
        }
    }
}