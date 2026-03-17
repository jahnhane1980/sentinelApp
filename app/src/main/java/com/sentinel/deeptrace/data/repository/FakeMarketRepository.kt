package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.MarketData

class FakeMarketRepository : MarketRepository {
    override suspend fun getMacroData(): MarketData {
        return MarketData(
            systemScore = 4.5,
            sp500Score = 8.2,
            nasdaqScore = 2.1,
            vix = 23.5,
            skew = 145.2,
            globalLiquidityM2 = "$94.2T",
            fedRepoFlow = "$620B",
            truflation = 2.8,
            isSimulation = true
        )
    }
}