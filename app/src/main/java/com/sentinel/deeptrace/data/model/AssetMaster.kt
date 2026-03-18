package com.sentinel.deeptrace.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets_master")
data class AssetMaster(
    @PrimaryKey val symbol: String,
    val fullName: String,
    val marketId: Int
)