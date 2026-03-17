package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.Asset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalWatchlistRepository : WatchlistRepository {

    private val _assets = MutableStateFlow<List<Asset>>(
        listOf(
            Asset(
                "USD/JPY", "Forex",
                relativeStrength = TODO()
            ),
            Asset(
                "S&P 500", "Index",
                relativeStrength = TODO()
            ),
            Asset(
                "Gold", "Commodity",
                relativeStrength = TODO()
            )
        )
    )

    override fun getWatchlistAssets(): Flow<List<Asset>> = _assets.asStateFlow()

    // HIER WICHTIG: Das 'suspend' muss davor stehen!
    override suspend fun addAsset(asset: Asset) {
        _assets.value = _assets.value + asset
    }

    // HIER WICHTIG: Das 'suspend' muss davor stehen!
    override suspend fun removeAsset(asset: Asset) {
        _assets.value = _assets.value - asset
    }
}