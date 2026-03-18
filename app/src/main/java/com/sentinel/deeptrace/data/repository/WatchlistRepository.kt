package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun getWatchlist(): Flow<List<WatchlistItem>>
    fun getWatchlistWithDetails(): Flow<List<WatchlistWithDetails>>

    suspend fun addAsset(symbol: String, name: String)
    suspend fun deleteAsset(symbol: String)
    suspend fun addTransaction(symbol: String, amount: Double, price: Double, currency: String)

    // WICHTIG: Das suspend sorgt dafür, dass die DB-Suche asynchron läuft
    suspend fun searchAssets(query: String): List<AssetMaster>

    fun getAvailableCurrencies(): Flow<List<String>>
    fun getMockMarketData(): MarketData?
}