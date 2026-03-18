package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.db.WatchlistDao
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LocalWatchlistRepository(
    private val watchlistDao: WatchlistDao
) : WatchlistRepository { // Diese Bindung löst den Fehler in der MainActivity

    override fun getWatchlist(): Flow<List<WatchlistItem>> =
        watchlistDao.getWatchlist()

    override fun getWatchlistWithDetails(): Flow<List<WatchlistWithDetails>> =
        watchlistDao.getWatchlistWithDetails()

    override suspend fun addAsset(symbol: String, name: String) {
        watchlistDao.insertWatchlist(WatchlistItem(symbol = symbol))
    }

    override suspend fun deleteAsset(symbol: String) {
        watchlistDao.deleteBySymbol(symbol)
    }

    override suspend fun addTransaction(symbol: String, amount: Double, price: Double, currency: String) {
        watchlistDao.insertTransaction(
            Transaction(
                symbol = symbol,
                amount = amount,
                totalPrice = price,
                currency = currency
            )
        )
    }

    // Die korrekte Umsetzung der Suche passend zum Interface
    override suspend fun searchAssets(query: String): List<AssetMaster> {
        if (query.isBlank()) return emptyList()
        // Wir fügen die Wildcards (%) hier hinzu, damit das DAO "Ap" als "%Ap%" sucht
        return watchlistDao.searchAssets("%$query%")
    }

    override fun getAvailableCurrencies(): Flow<List<String>> =
        flowOf(listOf("EUR", "USD", "JPY", "BTC"))

    override fun getMockMarketData(): MarketData =
        MarketData(
            systemScore = 8.5,
            sp500Score = 7.2,
            nasdaqScore = 6.8,
            vix = 20.0,
            skew = 140.0,
            usdJpy = 148.5,
            globalLiquidityM2 = 94.2,
            fedRepoFlow = 620.0,
            truflation = 2.8
        )
}