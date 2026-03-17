package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.MarketData

class FakeMarketRepository : MarketRepository {
    override suspend fun getMacroData() = MarketData(
        usdJpy = 159.3,      // "Professional Obfuscation" Phase
        fedRepoFlow = 620.0,
        vix = 23.5,          // Nervosität steigt
        sp500 = 5400.0,      // Squeeze-Modus Richtung Allzeithoch
        gold = 2180.0        // Smart Money geht in Deckung
    )
}