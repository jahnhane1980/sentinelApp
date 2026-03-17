package com.sentinel.deeptrace.core

object SentinelCore {
    fun calculateScore(usdJpy: Double, repo: Double, rs: Double): Double {
        // Layer 0: Liquidity Multiplier
        val lMult = when {
            usdJpy < 150.0 -> 0.2 // Yen-Carry-Trade Break
            repo > 500.0 -> 1.4   // Fed Injektion
            else -> 1.0
        }
        // Layer 1 & 2: Kombination mit Asset-Stärke (10-Punkte-System)
        return ((5.0 * lMult * 0.7) + (rs * 0.3)).coerceIn(1.0, 10.0)
    }
}