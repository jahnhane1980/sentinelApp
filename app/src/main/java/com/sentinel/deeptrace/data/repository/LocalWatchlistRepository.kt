package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.data.db.WatchlistDao
import kotlinx.coroutines.flow.Flow

class LocalWatchlistRepository(private val watchlistDao: WatchlistDao) : WatchlistRepository {

    // Liefert den Live-Stream der Datenbank an das UI
    override fun getWatchlist(): Flow<List<WatchlistItem>> {
        return watchlistDao.getAllStocks()
    }

    // Speichert eine neue Aktie in Room
    override suspend fun addStock(stock: WatchlistItem) {
        watchlistDao.insertStock(stock)
    }

    // Löscht eine Aktie aus Room
    override suspend fun removeStock(stock: WatchlistItem) {
        watchlistDao.deleteStock(stock)
    }
}