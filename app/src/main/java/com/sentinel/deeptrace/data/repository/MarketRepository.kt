interface MarketRepository {
    suspend fun getMacroData(): MarketData
}