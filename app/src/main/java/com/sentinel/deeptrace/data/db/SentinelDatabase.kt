package com.sentinel.deeptrace.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sentinel.deeptrace.data.model.WatchlistItem

@Database(entities = [WatchlistItem::class], version = 1, exportSchema = false)
abstract class SentinelDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: SentinelDatabase? = null

        fun getDatabase(context: Context): SentinelDatabase {
            // Falls INSTANCE bereits existiert, diese zurückgeben
            return INSTANCE ?: synchronized(this) {
                // Falls nicht, Datenbank neu aufbauen
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SentinelDatabase::class.java,
                    "sentinel_database"
                )
                    // WICHTIG: Erlaubt Room, die DB bei Versionskonflikten neu zu erstellen
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}