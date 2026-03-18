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

@Database(entities = [WatchlistItem::class, AssetMaster::class, Market::class], version = 3, exportSchema = false)
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
                    .fallbackToDestructiveMigration()
                    .addCallback(SentinelDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SentinelDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        fillDatabaseFromJson(database, context)
                    }
                }
            }
        }

        private suspend fun fillDatabaseFromJson(database: SentinelDatabase, context: Context) {
            val dao = database.watchlistDao()
            try {
                val jsonString = context.assets.open("initial_assets.json").bufferedReader().use { it.readText() }
                val root = JSONObject(jsonString)

                val markets = root.getJSONArray("markets")
                for (i in 0 until markets.length()) {
                    val m = markets.getJSONObject(i)
                    dao.insertMarket(Market(m.getInt("id"), m.getString("name"), m.getString("region"), m.getString("suffix")))
                }

                val assets = root.getJSONArray("assets")
                for (i in 0 until assets.length()) {
                    val a = assets.getJSONObject(i)
                    val symbol = a.getString("symbol")
                    dao.insertAssetMaster(AssetMaster(symbol, a.getString("name"), a.getInt("marketId")))
                    if (a.has("isPermanent") && a.getBoolean("isPermanent")) {
                        dao.insertWatchlist(WatchlistItem(symbol, isPermanent = true))
                    }
                }

                val watchlist = root.getJSONArray("initial_watchlist")
                for (i in 0 until watchlist.length()) {
                    dao.insertWatchlist(WatchlistItem(watchlist.getString(i)))
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}