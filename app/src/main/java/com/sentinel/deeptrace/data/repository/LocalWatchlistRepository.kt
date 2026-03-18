package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.db.WatchlistDao
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

class LocalWatchlistRepository(private val watchlistDao: WatchlistDao) {

    // Diese Zeile verursachte den Fehler. Wir nutzen jetzt den Namen aus dem DAO:
    fun getWatchlist(): Flow<List<WatchlistItem>> = watchlistDao.getAllItems()

    suspend fun addStock(stock: WatchlistItem) {
        watchlistDao.insertStock(stock)
    }

    suspend fun removeStock(stock: WatchlistItem) {
        watchlistDao.deleteStock(stock)
    }
}