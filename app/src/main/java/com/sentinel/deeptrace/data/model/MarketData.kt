package com.sentinel.deeptrace.data.model

data class MarketData(
    val usdJpy: Double,
    val fedRepoFlow: Double, // Wichtig für deine Liquiditäts-Hypothese
    val vix: Double,         // Wichtig für das Hedging/VIX-Calls
    val sp500: Double,       // Für den S&P 500 Squeeze
    val gold: Double,        // Als Safe Haven Asset
    val timestamp: Long = System.currentTimeMillis()
)