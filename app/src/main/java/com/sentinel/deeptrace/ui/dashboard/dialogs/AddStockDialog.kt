package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    viewModel: SentinelViewModel
) {
    var query by remember { mutableStateOf("") }
    val suggestions by viewModel.searchResults.collectAsState()

    var selectedAsset by remember { mutableStateOf<AssetMaster?>(null) }
    var isSearching by remember { mutableStateOf(true) }

    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val userCurrency by viewModel.userCurrency.collectAsState()
    val selectedCurrency by remember { mutableStateOf(userCurrency) }

    // FIX: Suche triggert jetzt sofort ab dem 1. Buchstaben
    LaunchedEffect(query) {
        if (query.isNotEmpty() && isSearching) {
            viewModel.searchMasterAssets(query)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            // FIX: Rundung der Karte reduziert
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Asset hinzufügen",
                    style = MaterialTheme.typography.titleLarge,
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isSearching) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Symbol oder Name suchen") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        // FIX: Rundung des Textfeldes reduziert
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SentinelBlue,
                            unfocusedTextColor = SentinelBlue,
                            focusedBorderColor = SentinelBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier.heightIn(max = 200.dp).fillMaxWidth(),
                        color = Color.White,
                        tonalElevation = 2.dp,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        LazyColumn {
                            items(suggestions) { asset ->
                                ListItem(
                                    headlineContent = { Text(asset.fullName, color = SentinelBlue) },
                                    supportingContent = { Text(asset.symbol, color = Color.Gray) },
                                    modifier = Modifier.clickable {
                                        selectedAsset = asset
                                        query = asset.fullName
                                        isSearching = false
                                    },
                                    colors = ListItemDefaults.colors(containerColor = Color.White)
                                )
                                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                } else {
                    selectedAsset?.let { asset ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = SentinelBlue.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(asset.fullName, fontWeight = FontWeight.Bold, color = SentinelBlue)
                                    Text(asset.symbol, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                TextButton(onClick = {
                                    isSearching = true
                                    selectedAsset = null
                                    query = ""
                                }) {
                                    Text("Ändern", color = SentinelBlue)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = amount,
                                onValueChange = { amount = it },
                                label = { Text("Menge") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SentinelBlue, unfocusedTextColor = SentinelBlue)
                            )
                            OutlinedTextField(
                                value = price,
                                onValueChange = { price = it },
                                label = { Text("Preis") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SentinelBlue, unfocusedTextColor = SentinelBlue)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        // FIX: Weniger Rundung für Buttons
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, SentinelBlue)
                    ) {
                        Text("Abbrechen", color = SentinelBlue)
                    }

                    Button(
                        onClick = {
                            selectedAsset?.let { asset ->
                                viewModel.addStockWithValidation(asset.symbol, asset.fullName)
                                val dAmount = amount.toDoubleOrNull() ?: 0.0
                                val dPrice = price.toDoubleOrNull() ?: 0.0
                                if (dAmount != 0.0) {
                                    viewModel.bookTransaction(asset.symbol, dAmount, dPrice, selectedCurrency)
                                }
                                onDismiss()
                            }
                        },
                        enabled = selectedAsset != null,
                        modifier = Modifier.weight(1f).height(48.dp),
                        // FIX: Weniger Rundung für Buttons
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
                    ) {
                        Text("Speichern", color = Color.White)
                    }
                }
            }
        }
    }
}