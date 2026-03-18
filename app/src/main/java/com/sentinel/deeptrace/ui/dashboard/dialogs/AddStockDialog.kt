package com.sentinel.deeptrace.ui.dashboard.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.theme.*

@Composable
fun AddStockDialog(
    onDismiss: () -> Unit,
    viewModel: SentinelViewModel
) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<AssetMaster>()) }
    var selectedAsset by remember { mutableStateOf<AssetMaster?>(null) }

    // Hilfs-State um die Liste zu schließen, wenn ein Item gewählt wurde
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length >= 1 && isSearching) {
            suggestions = viewModel.searchMasterAssets(query)
        } else {
            suggestions = emptyList()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SentinelDimens.SpacingMedium),
            shape = MaterialTheme.shapes.large, // Nutzt den Radius aus dem Theme
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
                Text(
                    text = stringResource(R.string.add_asset_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = SentinelBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))

                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        isSearching = true // Suche wieder aktivieren bei Tippen
                        selectedAsset = null
                    },
                    label = { Text("Ticker oder Name suchen...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SentinelBlue,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        cursorColor = SentinelBlue
                    ),
                    singleLine = true
                )

                // Autocomplete Liste
                if (suggestions.isNotEmpty() && isSearching) {
                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = SentinelDimens.MaxAutocompleteHeight),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                        shape = MaterialTheme.shapes.small
                    ) {
                        LazyColumn {
                            items(suggestions) { asset ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedAsset = asset
                                            query = asset.fullName // Wert in Textfeld übertragen
                                            isSearching = false // Liste schließen
                                        }
                                        .padding(SentinelDimens.SpacingMedium)
                                ) {
                                    Text(asset.symbol, fontWeight = FontWeight.Bold, color = SentinelBlue)
                                    Text(asset.fullName, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                HorizontalDivider(color = SentinelBlue.copy(alpha = 0.1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(SentinelDimens.SpacingExtraLarge))

                // Aktions-Buttons
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            selectedAsset?.let {
                                viewModel.addStockWithValidation(it.symbol, it.fullName)
                                onDismiss()
                            }
                        },
                        enabled = selectedAsset != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(SentinelDimens.ButtonHeight), // Korrekte Höhe
                        shape = MaterialTheme.shapes.medium, // Korrekter Radius
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SentinelBlue,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(stringResource(R.string.btn_save), color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.btn_cancel), color = Color.Gray)
                    }
                }
            }
        }
    }
}