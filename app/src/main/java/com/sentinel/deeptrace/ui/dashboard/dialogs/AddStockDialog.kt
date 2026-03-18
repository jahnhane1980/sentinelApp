package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    viewModel: SentinelViewModel
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<AssetMaster>()) }
    var selectedAsset by remember { mutableStateOf<AssetMaster?>(null) }
    var isSearching by remember { mutableStateOf(true) }

    // Transaktions-States
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val availableCurrencies by viewModel.availableCurrencies.collectAsState()
    var selectedCurrency by remember { mutableStateOf("EUR") }

    LaunchedEffect(availableCurrencies) {
        if (availableCurrencies.isNotEmpty()) selectedCurrency = availableCurrencies.first()
    }

    LaunchedEffect(query) {
        if (query.length >= 1 && isSearching) {
            suggestions = viewModel.searchMasterAssets(query)
        } else {
            suggestions = emptyList()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(SentinelDimens.SpacingMedium),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
                Text(stringResource(R.string.add_asset_title), color = SentinelBlue, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it; isSearching = true; selectedAsset = null },
                    label = { Text("Ticker suchen...") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (suggestions.isNotEmpty() && isSearching) {
                    Card(modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp)) {
                        LazyColumn {
                            items(suggestions) { asset ->
                                Column(modifier = Modifier.fillMaxWidth().clickable {
                                    selectedAsset = asset
                                    query = asset.fullName
                                    isSearching = false
                                }.padding(SentinelDimens.SpacingMedium)) {
                                    Text(asset.symbol, fontWeight = FontWeight.Bold, color = SentinelBlue)
                                    Text(asset.fullName, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                // Eingabefelder erscheinen nur nach Auswahl
                selectedAsset?.let {
                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

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

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))

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
                    modifier = Modifier.fillMaxWidth().height(SentinelDimens.ButtonHeight),
                    colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
                ) {
                    Text(stringResource(R.string.btn_save))
                }
            }
        }
    }
}