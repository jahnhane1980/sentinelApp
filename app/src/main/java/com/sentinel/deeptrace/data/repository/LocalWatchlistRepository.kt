package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.db.WatchlistDao
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.*
import kotlinx.coroutines.flow.Flow

class LocalWatchlistRepository(private val watchlistDao: WatchlistDao) {
    fun getWatchlist(): Flow<List<WatchlistWithDetails>> = watchlistDao.getWatchlistWithDetails()

    // Wildcards (%) hier hinzufügen, damit das DAO nur den Text bekommt
    suspend fun searchMasterAssets(query: String): List<AssetMaster> =
        watchlistDao.searchAssets("%$query%")

    suspend fun getMasterAsset(symbol: String): AssetMaster? =
        watchlistDao.getAssetBySymbol(symbol)

    suspend fun exists(symbol: String): Boolean =
        watchlistDao.existsInWatchlist(symbol)

    suspend fun addStock(stock: WatchlistItem) =
        watchlistDao.insertWatchlist(stock)

    suspend fun removeStock(symbol: String) =
        watchlistDao.deleteWatchlist(WatchlistItem(symbol = symbol))
}