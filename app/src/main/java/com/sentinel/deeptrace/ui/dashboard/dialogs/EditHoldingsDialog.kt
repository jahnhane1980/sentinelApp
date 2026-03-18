package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.config.AppConfig
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.ui.theme.*
import androidx.compose.ui.unit.dp

@Composable
fun EditHoldingsDialog(
    item: WatchlistWithDetails,
    availableCurrencies: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, String) -> Unit
) {
    // Zustände für die Eingabefelder
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isBuy by remember { mutableStateOf(true) }

    // Währung ist fixiert, wenn bereits Transaktionen existieren, sonst Standard aus Config
    var selectedCurrency by remember {
        mutableStateOf(item.currency ?: AppConfig.UserPreferences.DEFAULT_CURRENCY)
    }

    // Haptisches Feedback bereitstellen
    val haptic = LocalHapticFeedback.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SentinelDimens.SpacingMedium),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
                // Titel
                Text(
                    text = stringResource(R.string.label_transaction_title, item.name),
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                // Kauf / Verkauf Switcher
                TabRow(
                    selectedTabIndex = if (isBuy) 0 else 1,
                    containerColor = Color.Transparent,
                    contentColor = SentinelBlue,
                    divider = {} // Entfernt die Standard-Trennlinie für einen cleaner Look
                ) {
                    Tab(
                        selected = isBuy,
                        onClick = { isBuy = true },
                        text = { Text(stringResource(R.string.label_buy)) }
                    )
                    Tab(
                        selected = !isBuy,
                        onClick = { isBuy = false },
                        text = { Text(stringResource(R.string.label_sell)) }
                    )
                }

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                // Wiederverwendbare Eingabefelder (Menge, Preis, Währung)
                TransactionFields(
                    amount = amount,
                    onAmountChange = { amount = it },
                    price = price,
                    onPriceChange = { price = it },
                    selectedCurrency = selectedCurrency,
                    onCurrencyChange = { selectedCurrency = it },
                    availableCurrencies = availableCurrencies,
                    isCurrencyLocked = item.currency != null // Währung sperren, wenn bereits gesetzt
                )

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))

                // Button-Reihe: Abbrechen & Buchen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SentinelDimens.SpacingSmall)
                ) {
                    // ABBRECHEN
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(SentinelDimens.ButtonHeight),
                        shape = MaterialTheme.shapes.small, // Eckiger Radius (4.dp)
                        border = BorderStroke(1.dp, SentinelBlue)
                    ) {
                        Text(text = stringResource(R.string.btn_cancel), color = SentinelBlue)
                    }

                    // BUCHEN
                    Button(
                        onClick = {
                            // Haptik auslösen, wenn in Config aktiviert
                            if (AppConfig.HAPTIC_FEEDBACK_ENABLED) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }

                            val dAmount = amount.toDoubleOrNull() ?: 0.0
                            val dPrice = price.toDoubleOrNull() ?: 0.0

                            // Vorzeichen basierend auf Kauf/Verkauf setzen
                            val finalAmount = if (isBuy) dAmount else -dAmount
                            val finalPrice = if (isBuy) dPrice else -dPrice

                            if (dAmount != 0.0) {
                                onConfirm(finalAmount, finalPrice, selectedCurrency)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(SentinelDimens.ButtonHeight),
                        shape = MaterialTheme.shapes.small, // Eckiger Radius (4.dp)
                        colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue)
                    ) {
                        Text(text = stringResource(R.string.btn_book_transaction))
                    }
                }
            }
        }
    }
}