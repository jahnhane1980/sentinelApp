package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.ui.components.SentinelPrimaryButton
import com.sentinel.deeptrace.ui.theme.SentinelBlue

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Neuer Watchlist-Ticker", color = SentinelBlue, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text("Symbol") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Anzeigename") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SentinelBlue, focusedLabelColor = SentinelBlue, cursorColor = SentinelBlue),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            SentinelPrimaryButton(
                text = "Hinzufügen",
                onClick = { onConfirm(symbol, name) },
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}