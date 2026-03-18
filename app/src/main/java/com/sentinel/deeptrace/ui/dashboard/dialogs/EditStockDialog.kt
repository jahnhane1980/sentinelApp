package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.theme.SentinelBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStockDialog(
    item: WatchlistItem,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var symbol by remember { mutableStateOf(item.symbol) }
    var name by remember { mutableStateOf(item.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Edit Asset",
                color = SentinelBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text("Ticker Symbol (e.g. BTC)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    // FIX: Nutze OutlinedTextFieldDefaults.colors()
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    )
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    // FIX: Nutze OutlinedTextFieldDefaults.colors()
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(symbol, name) },
                colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}