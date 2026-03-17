package com.sentinel.deeptrace.ui.dashboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.core.SentinelConfig
import com.sentinel.deeptrace.core.ObservationFrequency
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.repository.FakeMarketRepository
import com.sentinel.deeptrace.domain.GetSentinelScoreUseCase
import kotlinx.coroutines.launch

class SentinelViewModel : ViewModel() {

    private val repo = FakeMarketRepository()
    private val getSentinelScoreUseCase = GetSentinelScoreUseCase(repo)

    // State für das komplette Datenpaket (Cockpit + Details)
    var marketData by mutableStateOf<MarketData?>(null)
        private set

    // Deine bestehenden Status-Variablen
    var frequency by mutableStateOf(SentinelConfig.currentFrequency)
    var lastUpdate by mutableStateOf(System.currentTimeMillis())

    init {
        updateAnalysis() // Startet die erste Analyse beim Öffnen
    }

    fun updateAnalysis() {
        viewModelScope.launch {
            try {
                // Holt das gesamte Paket aus dem UseCase
                val newData = getSentinelScoreUseCase()

                marketData = newData
                lastUpdate = System.currentTimeMillis()

                Log.d("Sentinel", "Dashboard Daten aktualisiert: SystemScore=${newData.systemScore}")
            } catch (e: Exception) {
                Log.e("Sentinel", "Fehler bei der Analyse: ${e.message}")
            }
        }
    }

    fun toggleFrequency() {
        frequency = if (frequency == ObservationFrequency.WEEKLY) {
            ObservationFrequency.DAILY
        } else {
            ObservationFrequency.WEEKLY
        }
        SentinelConfig.currentFrequency = frequency
        updateAnalysis()
    }
}