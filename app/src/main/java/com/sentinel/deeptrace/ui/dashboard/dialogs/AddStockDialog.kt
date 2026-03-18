package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.ui.components.SentinelPrimaryButton
import com.sentinel.deeptrace.ui.theme.SentinelBlue
import com.sentinel.deeptrace.ui.theme.SentinelDimens

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.add_asset_title),
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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    ),
                    shape = MaterialTheme.shapes.medium,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_display_name), style = MaterialTheme.typography.labelSmall) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        focusedLabelColor = SentinelBlue,
                        cursorColor = SentinelBlue
                    ),
                    shape = MaterialTheme.shapes.medium,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true
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
        },
        containerColor = Color.White,
        shape = MaterialTheme.shapes.extraLarge
    )
}