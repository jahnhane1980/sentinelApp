package com.sentinel.deeptrace.data.db

import androidx.room.*
import com.sentinel.deeptrace.data.model.* // Importiert: WatchlistItem, AssetMaster, Market, Transaction
import com.sentinel.deeptrace.data.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * UI-Modell für die Watchlist inkl. Portfolio-Berechnungen.
 * Hinweis: Room benötigt für berechnete Felder in @Query keine @Entity-Annotation hier.
 */
data class WatchlistWithDetails(
    val symbol: String,
    val name: String,
    val marketName: String,
    val score: Double,
    val isPermanent: Boolean,
    val totalHoldings: Double = 0.0,
    val totalInvested: Double = 0.0,
    val currency: String? = null
)

@Dao
interface WatchlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarket(market: Market): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssetMaster(asset: AssetMaster)

    @Query("SELECT region FROM markets")
    suspend fun getAllMarketCurrencies(): List<String>

    @Query("SELECT * FROM assets_master WHERE symbol LIKE :query OR fullName LIKE :query LIMIT 15")
    suspend fun searchAssets(query: String): List<AssetMaster>

    @Query("SELECT * FROM assets_master WHERE symbol = :symbol")
    suspend fun getAssetBySymbol(symbol: String): AssetMaster?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchlist(item: WatchlistItem)

    @Query("SELECT * FROM watchlist WHERE symbol = :symbol")
    suspend fun getWatchlistItemBySymbol(symbol: String): WatchlistItem?

    @Query("SELECT EXISTS(SELECT * FROM watchlist WHERE symbol = :symbol)")
    suspend fun existsInWatchlist(symbol: String): Boolean

    @Delete
    suspend fun deleteWatchlist(item: WatchlistItem)

    // --- Portfolio & Transaktionen ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE symbol = :symbol ORDER BY timestamp DESC")
    fun getTransactionsForAsset(symbol: String): Flow<List<Transaction>>

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
        ORDER BY w.isPermanent DESC, w.addedAt DESC
    """)
    fun getWatchlistWithDetails(): Flow<List<WatchlistWithDetails>>
}