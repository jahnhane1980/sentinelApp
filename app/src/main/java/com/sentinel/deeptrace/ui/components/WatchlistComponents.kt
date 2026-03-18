package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchlistItemComponent(item: WatchlistItem, onDelete: () -> Unit, onEdit: () -> Unit) {
    var showActions by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val scoreColor = if (item.score >= 7.5) SentinelBlue else if (item.score >= 4.0) SentinelOrange else SentinelRed

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(SentinelCardSurface, RoundedCornerShape(12.dp))
                .combinedClickable(
                    onClick = { if (showActions) showActions = false },
                    onLongClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); showActions = true }
                ).padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.name, fontWeight = FontWeight.Bold, color = SentinelBlue)
            Surface(color = scoreColor.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                Text(String.format("%.1f", item.score), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = scoreColor, fontWeight = FontWeight.Bold)
            }
        }
        AnimatedVisibility(visible = showActions, enter = expandHorizontally(), exit = shrinkHorizontally()) {
            Row(modifier = Modifier.padding(start = 8.dp)) {
                IconButton(onClick = { onEdit(); showActions = false }, modifier = Modifier.background(SentinelBlue, RoundedCornerShape(12.dp)).size(40.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { onDelete(); showActions = false }, modifier = Modifier.background(SentinelRed, RoundedCornerShape(12.dp)).size(40.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}