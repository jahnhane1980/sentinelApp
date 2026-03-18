package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.ui.theme.SentinelBlue
import com.sentinel.deeptrace.ui.theme.SentinelCardBlue
import com.sentinel.deeptrace.ui.theme.SentinelDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SentinelViewModel,
    modifier: Modifier = Modifier,
    onSettingInteracted: () -> Unit // Dieser Callback ist zwingend erforderlich!
) {
    val scrollState = rememberScrollState()
    val currentCurrency by viewModel.userCurrency.collectAsState()
    val hapticEnabled by viewModel.isHapticActive.collectAsState()
    val availableCurrencies by viewModel.availableCurrencies.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Einstellungen",
            style = MaterialTheme.typography.headlineMedium,
            color = SentinelBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Allgemein", style = MaterialTheme.typography.titleLarge, color = SentinelBlue)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Standard-Währung", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableCurrencies.forEach { currency ->
                        FilterChip(
                            selected = currentCurrency == currency,
                            onClick = {
                                viewModel.updateDefaultCurrency(currency)
                                onSettingInteracted()
                            },
                            label = { Text(currency) },
                            shape = RoundedCornerShape(4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SentinelBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Haptik Feedback", color = Color.White)
                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = {
                            viewModel.toggleHaptic(it)
                            onSettingInteracted()
                        },
                        colors = SwitchDefaults.colors(checkedTrackColor = SentinelBlue)
                    )
                }
            }
        }

        GoogleApiKeySetting(viewModel, onSettingInteracted)
    }
}

@Composable
fun GoogleApiKeySetting(viewModel: SentinelViewModel, onSettingInteracted: () -> Unit) {
    val apiKey by viewModel.googleApiKey.collectAsState()
    var showInfo by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Gemini API Key", style = MaterialTheme.typography.titleLarge, color = SentinelBlue, modifier = Modifier.weight(1f))
                IconButton(onClick = { showInfo = true }) {
                    Icon(Icons.Default.Info, null, tint = SentinelBlue)
                }
            }

            OutlinedTextField(
                value = apiKey,
                onValueChange = {
                    viewModel.updateGoogleApiKey(it)
                    onSettingInteracted()
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Hier Key einfügen...", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = SentinelBlue,
                    unfocusedTextColor = SentinelBlue,
                    focusedBorderColor = SentinelBlue
                )
            )
        }
    }

    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            confirmButton = {
                TextButton(onClick = { uriHandler.openUri("https://aistudio.google.com/app/apikey") }) {
                    Text("Zu AI Studio", color = SentinelBlue)
                }
            },
            title = { Text("Information") },
            text = { Text("Erhältlich im Google AI Studio.") }
        )
    }
}