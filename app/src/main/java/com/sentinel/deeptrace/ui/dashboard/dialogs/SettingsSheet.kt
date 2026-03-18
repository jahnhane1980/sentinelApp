package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    onDismiss: () -> Unit,
    viewModel: SentinelViewModel
) {
    val sheetState = rememberModalBottomSheetState()

    // Daten aus dem ViewModel beobachten
    val currentCurrency by viewModel.userCurrency.collectAsState()
    val hapticEnabled by viewModel.isHapticActive.collectAsState()
    val currencies by viewModel.availableCurrencies.collectAsState()

    var showCurrencyPicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SentinelCardBlue,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SentinelDimens.SpacingMedium)
                .padding(bottom = 48.dp) // Platz für Navigation-Bar
        ) {
            Text(
                text = stringResource(R.string.label_settings),
                style = MaterialTheme.typography.titleLarge,
                color = SentinelBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(SentinelDimens.SpacingLarge))

            // SEKTION: WÄHRUNG
            SettingsRow(
                label = "Standard-Währung",
                subLabel = "Basis für Portfolio-Gesamtwert",
                value = currentCurrency,
                onClick = { showCurrencyPicker = true }
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.05f), modifier = Modifier.padding(vertical = 8.dp))

            // SEKTION: HAPTIK
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SentinelDimens.SpacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Haptisches Feedback", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    Text("Vibration bei Interaktionen", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
                Switch(
                    checked = hapticEnabled,
                    onCheckedChange = { viewModel.toggleHaptic(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SentinelBlue,
                        checkedTrackColor = SentinelBlue.copy(alpha = 0.3f)
                    )
                )
            }

            // Einfacher Währungs-Picker (Dropdown-Alternative)
            if (showCurrencyPicker) {
                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))
                Text("Währung wählen", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currencies.forEach { currency ->
                        val isSelected = currency == currentCurrency
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.updateDefaultCurrency(currency)
                                showCurrencyPicker = false
                            },
                            label = { Text(currency) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SentinelBlue,
                                selectedLabelColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.small
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    label: String,
    subLabel: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = SentinelDimens.SpacingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            Text(subLabel, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, color = SentinelBlue, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}