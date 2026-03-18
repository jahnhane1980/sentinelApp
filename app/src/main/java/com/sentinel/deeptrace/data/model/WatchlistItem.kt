package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val symbol: String,
    val score: Double = 0.0,
    val isPermanent: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
    val holdings: Double = 0.0,
    val investedCapital: Double = 0.0,
    val currency: String? = null // Wird beim ersten Kauf gesetzt
)