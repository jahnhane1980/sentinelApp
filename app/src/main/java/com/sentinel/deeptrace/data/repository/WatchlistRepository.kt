package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun getWatchlist(): Flow<List<WatchlistItem>>
    suspend fun addStock(stock: WatchlistItem)
    suspend fun removeStock(stock: WatchlistItem)
}