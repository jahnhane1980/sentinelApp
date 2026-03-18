package com.sentinel.deeptrace.data.db

import androidx.room.*
import com.sentinel.deeptrace.data.model.*
import kotlinx.coroutines.flow.Flow

// Die Klasse MUSS hier stehen, damit sie für Room und UI sichtbar ist
data class WatchlistWithDetails(
    val symbol: String,
    val name: String,
    val marketName: String,
    val score: Double,
    val isPermanent: Boolean
)

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarket(market: Market): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssetMaster(asset: AssetMaster)

    @Query("SELECT * FROM assets_master WHERE symbol LIKE :query OR fullName LIKE :query LIMIT 15")
    suspend fun searchAssets(query: String): List<AssetMaster>

    @Query("SELECT * FROM assets_master WHERE symbol = :symbol")
    suspend fun getAssetBySymbol(symbol: String): AssetMaster?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlist(item: WatchlistItem)

    @Query("SELECT EXISTS(SELECT * FROM watchlist WHERE symbol = :symbol)")
    suspend fun existsInWatchlist(symbol: String): Boolean

    @Delete
    suspend fun deleteWatchlist(item: WatchlistItem)

    @Query("""
        SELECT w.symbol, a.fullName as name, m.name as marketName, w.score, w.isPermanent 
        FROM watchlist w
        JOIN assets_master a ON w.symbol = a.symbol
        JOIN markets m ON a.marketId = m.id
        ORDER BY w.isPermanent DESC, w.addedAt DESC
    """)
    fun getWatchlistWithDetails(): Flow<List<WatchlistWithDetails>>
}