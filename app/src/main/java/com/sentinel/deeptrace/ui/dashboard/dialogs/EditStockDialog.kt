package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.ui.components.SentinelPrimaryButton
import com.sentinel.deeptrace.ui.theme.SentinelBlue
import com.sentinel.deeptrace.ui.theme.SentinelDimens

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
        shape = MaterialTheme.shapes.extraLarge,
        title = {
            Text(
                text = stringResource(R.string.edit_asset_title),
                style = MaterialTheme.typography.titleMedium,
                color = SentinelBlue
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(SentinelDimens.SpacingSmall)) {
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it.uppercase() },
                    label = { Text(stringResource(R.string.label_ticker_symbol), style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    )
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_display_name), style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    )
                )
            }
        },
        confirmButton = {
            SentinelPrimaryButton(
                text = stringResource(R.string.btn_save),
                onClick = { onConfirm(symbol, name) }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.btn_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
            }
        }
    )
}