package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.db.WatchlistDao
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

class LocalWatchlistRepository(private val watchlistDao: WatchlistDao) {

    // Liefert den Live-Stream der Watchlist aus der DB
    fun getWatchlist(): Flow<List<WatchlistItem>> = watchlistDao.getAllItems()

    // Prüft, ob ein Ticker-Symbol bereits existiert (wichtig für UI-Feedback)
    suspend fun exists(symbol: String): Boolean {
        return watchlistDao.exists(symbol)
    }

    suspend fun addStock(stock: WatchlistItem) {
        watchlistDao.insertStock(stock)
    }

    suspend fun updateStock(stock: WatchlistItem) {
        watchlistDao.updateStock(stock)
    }

    suspend fun removeStock(stock: WatchlistItem) {
        watchlistDao.deleteStock(stock)
    }
}