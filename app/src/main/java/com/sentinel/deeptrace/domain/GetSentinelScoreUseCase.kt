package com.sentinel.deeptrace.domain

import com.sentinel.deeptrace.core.SentinelCore
import com.sentinel.deeptrace.data.repository.MarketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSentinelScoreUseCase(
    private val repository: MarketRepository
) {
    suspend operator fun invoke(): Double = withContext(Dispatchers.Default) {
        val data = repository.getMacroData()

        // WICHTIG: Kein 'val core = ...' mehr nötig!
        // Wir greifen direkt auf das 'object' zu:
        return@withContext SentinelCore.calculateScore(
            usdJpy = data.usdJpy,
            fedRepoFlow = data.fedRepoFlow,
            vix = data.vix,
            sp500 = data.sp500,
            gold = data.gold
        )
    }
}