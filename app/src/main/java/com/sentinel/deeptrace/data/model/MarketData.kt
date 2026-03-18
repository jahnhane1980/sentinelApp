package com.sentinel.deeptrace.data.model

data class MarketData(
    val systemScore: Double,
    val sp500Score: Double,
    val nasdaqScore: Double,
    val vix: Double,
    val skew: Double,
    val usdJpy: Double,
    val globalLiquidityM2: Double, // Double statt String
    val fedRepoFlow: Double,       // Double statt String
    val truflation: Double,
    val isSimulation: Boolean = true
)