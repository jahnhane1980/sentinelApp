package com.sentinel.deeptrace.ui.dashboard

import android.util.Log // Fix für Log.e
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.core.SentinelConfig
import com.sentinel.deeptrace.core.ObservationFrequency
import com.sentinel.deeptrace.data.repository.FakeMarketRepository
import com.sentinel.deeptrace.domain.GetSentinelScoreUseCase
import kotlinx.coroutines.launch

class SentinelViewModel : ViewModel() {

    // 1. Den UseCase direkt hier initialisieren (da wir noch keine Dependency Injection nutzen)
    private val repo = FakeMarketRepository()
    private val getSentinelScoreUseCase = GetSentinelScoreUseCase(repo)

    // 2. Wir nutzen deine vorhandenen Variablen (einfacher Weg)
    var currentScore by mutableStateOf(0.0)
    var frequency by mutableStateOf(SentinelConfig.currentFrequency)
    var lastUpdate by mutableStateOf(System.currentTimeMillis())

    fun updateAnalysis() {
        viewModelScope.launch {
            try {
                // Hier rufen wir den UseCase auf
                val newScore = getSentinelScoreUseCase()

                // Wir weisen den Wert direkt deinen Variablen zu
                currentScore = newScore
                lastUpdate = System.currentTimeMillis()

                Log.d("Sentinel", "Neuer Score berechnet: $newScore")
            } catch (e: Exception) {
                Log.e("Sentinel", "Fehler bei der Analyse: ${e.message}")
            }
        }
    }

    fun toggleFrequency() {
        // Fix für ObservationFrequency (wir nutzen die Werte aus deinem Enum)
        frequency = if (frequency == ObservationFrequency.WEEKLY) {
            ObservationFrequency.DAILY
        } else {
            ObservationFrequency.WEEKLY
        }
        SentinelConfig.currentFrequency = frequency
        updateAnalysis()
    }
}