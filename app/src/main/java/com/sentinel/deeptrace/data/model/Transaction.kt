package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = WatchlistItem::class,
            parentColumns = ["symbol"],
            childColumns = ["symbol"],
            onDelete = ForeignKey.CASCADE // DAS hier löscht die "Leichen"
        )
    ],
    indices = [Index("symbol")] // Index für schnellere Abfragen
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val amount: Double,
    val totalPrice: Double,
    val currency: String,
    val timestamp: Long = System.currentTimeMillis()
)