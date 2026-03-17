package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.sentinel.deeptrace.core.*
import com.sentinel.deeptrace.data.repository.*
import kotlinx.coroutines.launch

class SentinelViewModel : ViewModel() {
    private val repo: MarketRepository = if (SentinelConfig.USE_MOCK_DATA) FakeMarketRepository() else FakeMarketRepository()
    
    var currentScore by mutableStateOf(0.0)
    var frequency by mutableStateOf(SentinelConfig.currentFrequency)

    fun updateAnalysis() {
        viewModelScope.launch {
            val data = repo.getMacroData()
            // Beispielhaft für Navitas (RS 8.5)
            currentScore = SentinelCore.calculateScore(data.usdJpy, data.fedRepoFlow, 8.5)
        }
    }

    fun toggleFrequency() {
        frequency = if (frequency == ObservationFrequency.WEEKLY) ObservationFrequency.FOUR_HOURS else ObservationFrequency.WEEKLY
        SentinelConfig.currentFrequency = frequency
        updateAnalysis()
    }
}