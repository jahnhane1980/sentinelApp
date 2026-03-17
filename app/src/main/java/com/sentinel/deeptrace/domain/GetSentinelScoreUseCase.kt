package com.sentinel.deeptrace.domain

import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.repository.MarketRepository

class GetSentinelScoreUseCase(private val repository: MarketRepository) {
    // Gibt jetzt das komplette Datenpaket zurück statt nur eine Zahl
    suspend operator fun invoke(): MarketData {
        return repository.getMacroData()
    }
}