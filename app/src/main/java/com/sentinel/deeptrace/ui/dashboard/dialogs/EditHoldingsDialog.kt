package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.SentinelBlue

@Composable
fun EditHoldingsDialog(
    item: WatchlistWithDetails,
    availableCurrencies: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isSell by remember { mutableStateOf(false) }
    val selectedCurrency by remember { mutableStateOf(item.currency ?: "EUR") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(8.dp), // Kanten-Fix
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "${item.symbol} Transaktion",
                    style = MaterialTheme.typography.titleLarge,
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isSell) "Verkauf (-)" else "Kauf (+)",
                        color = if (isSell) Color.Red else Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = isSell,
                        onCheckedChange = { isSell = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = if (isSell) Color.Red else SentinelBlue)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Menge") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp), // Kanten-Fix
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SentinelBlue,
                        unfocusedTextColor = SentinelBlue,
                        focusedBorderColor = SentinelBlue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Gesamtpreis") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp), // Kanten-Fix
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SentinelBlue,
                        unfocusedTextColor = SentinelBlue,
                        focusedBorderColor = SentinelBlue
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Abbrechen", color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            val a = amount.toDoubleOrNull() ?: 0.0
                            val p = price.toDoubleOrNull() ?: 0.0
                            val finalAmount = if (isSell) -a else a
                            onConfirm(finalAmount, p, selectedCurrency)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(4.dp), // Kanten-Fix
                        colors = ButtonDefaults.buttonColors(containerColor = if (isSell) Color.Red else SentinelBlue)
                    ) {
                        Text(if (isSell) "Buchen" else "Speichern", color = Color.White)
                    }
                }
            }
        }
    }
}