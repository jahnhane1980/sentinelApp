package com.sentinel.deeptrace.data.db

import androidx.room.*
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAllItems(): Flow<List<WatchlistItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStock(item: WatchlistItem): Long

    @Update
    suspend fun updateStock(item: WatchlistItem)

    @Delete
    suspend fun deleteStock(item: WatchlistItem)

    @Query("SELECT EXISTS(SELECT * FROM watchlist WHERE symbol = :symbol)")
    suspend fun exists(symbol: String): Boolean
}