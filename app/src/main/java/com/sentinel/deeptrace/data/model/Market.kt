package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markets")
data class Market(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val region: String,
    val suffix: String
)