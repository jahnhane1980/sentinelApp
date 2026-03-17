package com.sentinel.deeptrace.data.model

data class MarketData(
    val systemScore: Double,
    val sp500Score: Double,
    val nasdaqScore: Double,
    val vix: Double,
    val skew: Double,
    val globalLiquidityM2: String,
    val fedRepoFlow: String,
    val truflation: Double,
    val isSimulation: Boolean = true
)