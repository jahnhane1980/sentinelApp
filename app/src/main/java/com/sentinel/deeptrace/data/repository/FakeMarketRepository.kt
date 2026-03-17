class FakeMarketRepository : MarketRepository {
    override suspend fun getMacroData() = MarketData(
        usdJpy = 159.3, // "Professional Obfuscation" Phase
        fedRepoFlow = 620.0, 
        vix = 23.5
    )
}