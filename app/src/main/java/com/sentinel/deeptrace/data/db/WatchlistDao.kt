package com.sentinel.deeptrace.data.db

import androidx.room.*
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllStocks(): Flow<List<WatchlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: WatchlistItem)

    @Delete
    suspend fun deleteStock(stock: WatchlistItem)
}