package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.core.SentinelConfig
import com.sentinel.deeptrace.core.SentinelCore
import com.sentinel.deeptrace.data.repository.MarketRepository
import com.sentinel.deeptrace.data.repository.FakeMarketRepository
import com.sentinel.deeptrace.core.ObservationFrequency
import kotlinx.coroutines.launch

class SentinelViewModel : ViewModel() {
    private val repo: MarketRepository = if (SentinelConfig.USE_MOCK_DATA) {
        FakeMarketRepository()
    } else {
        // Hier später das echte Repo
        FakeMarketRepository()
    }

    var currentScore by mutableStateOf(0.0)
    var frequency by mutableStateOf(SentinelConfig.currentFrequency)

// ... innerhalb deiner SentinelViewModel Klasse

    fun updateAnalysis() {
        viewModelScope.launch {
            try {
                // Wir nutzen den UseCase, den wir gebaut haben.
                // Dieser holt sich die Daten selbstständig aus dem Repository
                // und füttert den SentinelCore mit ALLEN 5 Parametern.
                val newScore = getSentinelScoreUseCase()

                _uiState.value = _uiState.value.copy(
                    score = newScore,
                    lastUpdate = System.currentTimeMillis()
                )
            } catch (e: Exception) {
                // Fehlerbehandlung, falls z.B. das Repository keine Daten liefert
                Log.e("Sentinel", "Fehler bei der Analyse", e)
            }
        }
    }

    fun toggleFrequency() {
        frequency = if (frequency == ObservationFrequency.WEEKLY) ObservationFrequency.DAILY else ObservationFrequency.WEEKLY
        SentinelConfig.currentFrequency = frequency
        updateAnalysis()
    }
}