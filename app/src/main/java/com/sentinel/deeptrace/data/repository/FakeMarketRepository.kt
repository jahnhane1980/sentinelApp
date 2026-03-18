package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.MarketData

class FakeMarketRepository : MarketRepository {
    override suspend fun getMacroData(): MarketData {
        return MarketData(
            systemScore = 0.0, // Wird jetzt live im UseCase berechnet!
            sp500Score = 8.2,
            nasdaqScore = 2.1,
            vix = 23.5,
            skew = 145.2,
            usdJpy = 148.5,        // Beispiel für Yen-Schwäche (< 150)
            globalLiquidityM2 = 94.2,
            fedRepoFlow = 620.0,
            truflation = 2.8,
            isSimulation = true
        )
    }
}