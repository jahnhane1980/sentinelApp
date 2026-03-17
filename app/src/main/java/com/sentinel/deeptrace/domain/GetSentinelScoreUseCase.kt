package com.sentinel.deeptrace.domain

import com.sentinel.deeptrace.core.SentinelCore
import com.sentinel.deeptrace.data.repository.MarketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSentinelScoreUseCase(private val repository: MarketRepository) {
    suspend operator fun invoke(): Double {
        val data = repository.getMacroData()
        // Hier greifen wir direkt auf das 'object' SentinelCore zu
        return SentinelCore.calculateScore(
            usdJpy = data.usdJpy,
            fedRepoFlow = data.fedRepoFlow,
            vix = data.vix,
            sp500 = data.sp500,
            gold = data.gold
        )
    }
}