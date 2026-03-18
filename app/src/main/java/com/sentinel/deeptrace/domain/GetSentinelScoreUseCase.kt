package com.sentinel.deeptrace.domain

import com.sentinel.deeptrace.core.SentinelCore
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.repository.MarketRepository

class GetSentinelScoreUseCase(private val repository: MarketRepository) {

    suspend operator fun invoke(): MarketData {
        // 1. Hol die statischen Daten aus dem FakeRepository
        val rawData = repository.getMacroData()

        // 2. Berechne den echten System-Score basierend auf den Werten
        val calculatedScore = SentinelCore.calculateScore(
            usdJpy = rawData.usdJpy,
            fedRepoFlow = rawData.fedRepoFlow,
            vix = rawData.vix,
            sp500 = rawData.sp500Score,
            gold = 8.5 // Beispielwert für Gold-Stärke
        )

        // 3. Gib die Daten mit dem neu berechneten Score zurück
        return rawData.copy(systemScore = calculatedScore)
    }
}