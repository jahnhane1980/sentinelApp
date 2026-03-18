package com.sentinel.deeptrace.ui.dashboard

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.model.*
import com.sentinel.deeptrace.data.repository.*
import com.sentinel.deeptrace.domain.GetSentinelScoreUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SentinelViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SentinelDatabase.getDatabase(application)
    private val watchlistRepo = LocalWatchlistRepository(db.watchlistDao())
    private val getSentinelScoreUseCase = GetSentinelScoreUseCase(FakeMarketRepository())

    var marketData by mutableStateOf<MarketData?>(null)
    var uiErrorMessage by mutableStateOf<String?>(null)

    val watchlist = watchlistRepo.getWatchlist().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init { updateAnalysis() }
    fun updateAnalysis() { viewModelScope.launch { marketData = getSentinelScoreUseCase() } }
    fun clearError() { uiErrorMessage = null }
    suspend fun searchMasterAssets(query: String) = watchlistRepo.searchMasterAssets(query)

    fun addStockWithValidation(symbol: String, name: String) {
        val app = getApplication<Application>()
        viewModelScope.launch {
            val masterAsset = watchlistRepo.getMasterAsset(symbol)
            when {
                masterAsset == null -> uiErrorMessage = app.getString(R.string.error_ticker_not_found, symbol)
                masterAsset.fullName != name -> uiErrorMessage = app.getString(R.string.error_name_mismatch, symbol)
                watchlistRepo.exists(symbol) -> uiErrorMessage = app.getString(R.string.error_already_in_watchlist, symbol)
                else -> watchlistRepo.addStock(WatchlistItem(symbol = symbol))
            }
        }
    }

    fun removeStock(symbol: String) = viewModelScope.launch { watchlistRepo.removeStock(symbol) }
}