package com.sentinel.deeptrace.data.db

import androidx.room.*
// Wir importieren deine spezifische Transaction-Klasse
import com.sentinel.deeptrace.data.model.Transaction
import com.sentinel.deeptrace.data.model.Market
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    // --- Master Data (Asset Katalog) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarket(market: Market): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssetMaster(asset: AssetMaster)

    @Query("SELECT * FROM assets_master WHERE symbol LIKE :query OR fullName LIKE :query LIMIT 15")
    suspend fun searchAssets(query: String): List<AssetMaster>

    @Query("SELECT * FROM assets_master WHERE symbol = :symbol LIMIT 1")
    suspend fun getAssetBySymbol(symbol: String): AssetMaster?

    // --- Watchlist Kern-Funktionen ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlist(item: WatchlistItem)

    @Query("SELECT * FROM watchlist WHERE symbol = :symbol LIMIT 1")
    suspend fun getWatchlistItemBySymbol(symbol: String): WatchlistItem?

    @Query("SELECT EXISTS(SELECT * FROM watchlist WHERE symbol = :symbol)")
    suspend fun existsInWatchlist(symbol: String): Boolean

    @Delete
    suspend fun deleteWatchlist(item: WatchlistItem)

    @Query("DELETE FROM watchlist WHERE symbol = :symbol")
    suspend fun deleteBySymbol(symbol: String)

    // --- Transaktionen & Portfolio ---

    // Hier wird nun deine Transaction-Klasse aus den Models genutzt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE symbol = :symbol ORDER BY timestamp DESC")
    fun getTransactionsForAsset(symbol: String): Flow<List<Transaction>>

    /**
     * Die Haupt-Query für das Dashboard.
     * Nutzt die externe Klasse WatchlistWithDetails.
     */
    @Query("""
        SELECT 
            w.symbol, 
            a.fullName as name, 
            m.name as marketName, 
            w.score, 
            w.isPermanent,
            TOTAL(t.amount) as totalHoldings,
            TOTAL(t.totalPrice) as totalInvested,
            (SELECT currency FROM transactions WHERE symbol = w.symbol LIMIT 1) as currency
        FROM watchlist w
        JOIN assets_master a ON w.symbol = a.symbol
        JOIN markets m ON a.marketId = m.id
        LEFT JOIN transactions t ON w.symbol = t.symbol
        GROUP BY w.symbol
    """)
    fun getWatchlistWithDetails(): Flow<List<WatchlistWithDetails>>

    @Query("SELECT * FROM watchlist")
    fun getWatchlist(): Flow<List<WatchlistItem>>
}