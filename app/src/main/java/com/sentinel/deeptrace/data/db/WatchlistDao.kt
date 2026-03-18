package com.sentinel.deeptrace.data.db

import androidx.room.*
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAllItems(): Flow<List<WatchlistItem>> // Hier muss der Name übereinstimmen!

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(item: WatchlistItem)

    @Delete
    suspend fun deleteStock(item: WatchlistItem)
}