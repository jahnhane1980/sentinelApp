package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment

@Composable
fun EditHoldingsDialog(
    item: WatchlistWithDetails,
    availableCurrencies: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isBuy by remember { mutableStateOf(true) }
    var selectedCurrency by remember { mutableStateOf(item.currency ?: availableCurrencies.firstOrNull() ?: "EUR") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(SentinelDimens.SpacingMedium),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
                Text(stringResource(R.string.label_transaction_title, item.name), color = SentinelBlue, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                TabRow(
                    selectedTabIndex = if (isBuy) 0 else 1,
                    containerColor = Color.Transparent,
                    contentColor = SentinelBlue
                ) {
                    Tab(selected = isBuy, onClick = { isBuy = true }) {
                        Text(stringResource(R.string.label_buy), modifier = Modifier.padding(SentinelDimens.SpacingSmall))
                    }
                    Tab(selected = !isBuy, onClick = { isBuy = false }) {
                        Text(stringResource(R.string.label_sell), modifier = Modifier.padding(SentinelDimens.SpacingSmall))
                    }
                }

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                TransactionFields(
                    amount = amount,
                    onAmountChange = { amount = it },
                    price = price,
                    onPriceChange = { price = it },
                    selectedCurrency = selectedCurrency,
                    onCurrencyChange = { selectedCurrency = it },
                    availableCurrencies = availableCurrencies,
                    isCurrencyLocked = item.currency != null
                )

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))

                Button(
                    onClick = {
                        val dAmount = (amount.toDoubleOrNull() ?: 0.0) * (if (isBuy) 1 else -1)
                        val dPrice = (price.toDoubleOrNull() ?: 0.0) * (if (isBuy) 1 else -1)
                        onConfirm(dAmount, dPrice, selectedCurrency)
                    },
                    modifier = Modifier.fillMaxWidth().height(SentinelDimens.ButtonHeight),
                    colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
                ) {
                    Text(stringResource(R.string.btn_book_transaction))
                }
            }
        }
    }
}