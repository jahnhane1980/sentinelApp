package com.sentinel.deeptrace.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sentinel.deeptrace.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Database(
    entities = [
        WatchlistItem::class,
        AssetMaster::class,
        Market::class,
        Transaction::class // NEU hinzugefügt
    ],
    version = 6, // Transaction mit löschen, Automatisches Löschen (Foreign Key Cascade)
    exportSchema = false
)
abstract class SentinelDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: SentinelDatabase? = null

        fun getDatabase(context: Context): SentinelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SentinelDatabase::class.java,
                    "sentinel_database"
                )
                    .fallbackToDestructiveMigration() // Löscht DB bei Versionssprung (wichtig für Dev-Phase)
                    .addCallback(SentinelDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SentinelDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        fillDatabaseFromJson(database, context)
                    }
                }
            }
        }

        /**
         * Liest die initial_assets.json ein und befüllt Märkte sowie den Asset-Katalog.
         */
        private suspend fun fillDatabaseFromJson(database: SentinelDatabase, context: Context) {
            val dao = database.watchlistDao()
            try {
                val jsonString = context.assets.open("initial_assets.json")
                    .bufferedReader().use { it.readText() }
                val root = JSONObject(jsonString)

                // 1. Märkte importieren
                val markets = root.getJSONArray("markets")
                for (i in 0 until markets.length()) {
                    val m = markets.getJSONObject(i)
                    dao.insertMarket(
                        Market(
                            id = m.getInt("id"),
                            name = m.getString("name"),
                            region = m.getString("region"),
                            suffix = m.getString("suffix")
                        )
                    )
                }

                // 2. Asset Master Katalog importieren
                val assets = root.getJSONArray("assets")
                for (i in 0 until assets.length()) {
                    val a = assets.getJSONObject(i)
                    val symbol = a.getString("symbol")

                    dao.insertAssetMaster(
                        AssetMaster(
                            symbol = symbol,
                            fullName = a.getString("name"),
                            marketId = a.getInt("marketId")
                        )
                    )

                    // Falls als permanent markiert (System-Hedges)
                    if (a.has("isPermanent") && a.getBoolean("isPermanent")) {
                        dao.insertWatchlist(WatchlistItem(symbol = symbol, isPermanent = true))
                    }
                }

                // 3. Initiale Nutzer-Watchlist setzen
                if (root.has("initial_watchlist")) {
                    val watchlist = root.getJSONArray("initial_watchlist")
                    for (i in 0 until watchlist.length()) {
                        val symbol = watchlist.getString(i)
                        // Nur hinzufügen, wenn noch nicht durch "isPermanent" geschehen
                        if (!dao.existsInWatchlist(symbol)) {
                            dao.insertWatchlist(WatchlistItem(symbol = symbol))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}