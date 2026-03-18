package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.ui.theme.SentinelDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFields(
    amount: String,
    onAmountChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    selectedCurrency: String,
    onCurrencyChange: (String) -> Unit,
    availableCurrencies: List<String>,
    isCurrencyLocked: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(SentinelDimens.SpacingSmall)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (!isCurrencyLocked) expanded = it }
        ) {
            OutlinedTextField(
                value = selectedCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_currency)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = !isCurrencyLocked,
                shape = MaterialTheme.shapes.small
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableCurrencies.forEach { curr ->
                    DropdownMenuItem(
                        text = { Text(curr) },
                        onClick = {
                            onCurrencyChange(curr)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text(stringResource(R.string.label_amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )

        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text(stringResource(R.string.label_total_price, selectedCurrency)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )
    }
}