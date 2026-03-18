package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val symbol: String,
    val name: String,
    val score: Double = 0.0,
    val isPermanent: Boolean = false // NEU: Schützt vor Löschung
)