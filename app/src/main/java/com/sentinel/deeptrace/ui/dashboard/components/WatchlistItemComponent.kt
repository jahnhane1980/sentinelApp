package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.WatchlistWithDetails // Wichtig: Import prüfen!
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun WatchlistItemComponent(
    item: WatchlistWithDetails,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SentinelCardBlue),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(SentinelDimens.CardPadding).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name, // Wird jetzt gefunden, da der Typ oben stimmt
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = SentinelBlue
                )
                Text(
                    text = "${item.symbol} | ${item.marketName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, stringResource(R.string.action_delete), tint = SentinelRed)
            }
        }
    }
}