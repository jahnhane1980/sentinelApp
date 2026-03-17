package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val symbol: String, // z.B. "AAPL" oder "NVIDIA"
    val name: String,
    val score: Double = 0.0,
    val addedAt: Long = System.currentTimeMillis()
)