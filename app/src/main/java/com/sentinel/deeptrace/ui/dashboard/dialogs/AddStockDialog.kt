package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.config.AppConfig
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    viewModel: SentinelViewModel
) {
    var query by remember { mutableStateOf("") }
    // FIX: Wir holen die Vorschläge jetzt aus dem ViewModel
    val suggestions by viewModel.searchResults.collectAsState()

    var selectedAsset by remember { mutableStateOf<AssetMaster?>(null) }
    var isSearching by remember { mutableStateOf(true) }

    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val userCurrency by viewModel.userCurrency.collectAsState()
    val availableCurrencies by viewModel.availableCurrencies.collectAsState()
    var selectedCurrency by remember { mutableStateOf(userCurrency) }

    val haptic = LocalHapticFeedback.current

    // FIX: Die Suche wird getriggert, aber das Ergebnis kommt über den Flow oben
    LaunchedEffect(query) {
        if (query.length >= 1 && isSearching) {
            viewModel.searchMasterAssets(query)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(modifier = Modifier.padding(SentinelDimens.SpacingMedium)) {
                Text(
                    text = stringResource(R.string.add_asset_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                if (isSearching) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Symbol oder Name suchen") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))

                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(suggestions) { asset ->
                            ListItem(
                                headlineContent = { Text(asset.fullName) },
                                supportingContent = { Text(asset.symbol) },
                                modifier = Modifier.clickable {
                                    selectedAsset = asset
                                    query = asset.fullName
                                    isSearching = false
                                }
                            )
                        }
                    }
                } else {
                    // Anzeige des gewählten Assets und Transaktionsfelder
                    selectedAsset?.let { asset ->
                        Text("Ausgewählt: ${asset.fullName} (${asset.symbol})", color = SentinelBlue)
                        TextButton(onClick = { isSearching = true }) {
                            Text("Suche ändern", color = Color.Gray)
                        }

                        TransactionFields(
                            amount = amount,
                            onAmountChange = { amount = it },
                            price = price,
                            onPriceChange = { price = it },
                            selectedCurrency = selectedCurrency,
                            onCurrencyChange = { selectedCurrency = it },
                            availableCurrencies = availableCurrencies
                        )
                    }
                }

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))

                Row(horizontalArrangement = Arrangement.spacedBy(SentinelDimens.SpacingSmall)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(SentinelDimens.ButtonHeight),
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.dp, SentinelBlue)
                    ) {
                        Text(stringResource(R.string.btn_cancel), color = SentinelBlue)
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
                        modifier = Modifier.weight(1f).height(SentinelDimens.ButtonHeight),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
                    ) {
                        Text(stringResource(R.string.btn_save))
                    }
                }
            }
        }
    }
}