package com.sentinel.deeptrace.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sentinel.deeptrace.data.model.WatchlistItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [WatchlistItem::class], version = 1, exportSchema = false)
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
                    .addCallback(SentinelDatabaseCallback()) // Script beim Start
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SentinelDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.watchlistDao()
                        // Initiales Setup der Master-Hedges
                        dao.insertStock(WatchlistItem("XAU/USD", "GOLD (System Hedge)", 8.5, true))
                        dao.insertStock(WatchlistItem("USD/JPY", "YEN Carry Trace", 4.2, true))
                        dao.insertStock(WatchlistItem("SPX", "S&P 500 Index", 7.1, true))
                    }
                }
            }
        }
    }
}