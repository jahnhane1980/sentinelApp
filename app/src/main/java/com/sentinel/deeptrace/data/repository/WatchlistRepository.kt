package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.Asset
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    // Gibt eine Liste aller Assets zurück, die wir beobachten (z.B. USD/JPY, Gold)
    fun getWatchlistAssets(): Flow<List<Asset>>

    // Erlaubt es, ein neues Asset zur Beobachtung hinzuzufügen
    suspend fun addAsset(asset: Asset)

    // Erlaubt es, ein Asset zu entfernen
    suspend fun removeAsset(asset: Asset)
}