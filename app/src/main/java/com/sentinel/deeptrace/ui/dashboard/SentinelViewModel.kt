package com.sentinel.deeptrace.ui.dashboard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.data.repository.FakeMarketRepository
import com.sentinel.deeptrace.data.repository.LocalWatchlistRepository
import com.sentinel.deeptrace.domain.GetSentinelScoreUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SentinelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SentinelDatabase.getDatabase(application)
    private val watchlistRepo = LocalWatchlistRepository(db.watchlistDao())

    private val marketRepo = FakeMarketRepository()
    private val getSentinelScoreUseCase = GetSentinelScoreUseCase(marketRepo)

    var marketData by mutableStateOf<com.sentinel.deeptrace.data.model.MarketData?>(null)
        private set

    val watchlist = watchlistRepo.getWatchlist().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        updateAnalysis()
    }

    fun updateAnalysis() {
        viewModelScope.launch {
            marketData = getSentinelScoreUseCase()
        }
    }

    fun addStock(symbol: String, name: String) {
        viewModelScope.launch {
            watchlistRepo.addStock(WatchlistItem(symbol = symbol, name = name))
        }
    }

    fun removeStock(stock: WatchlistItem) {
        if (stock.isPermanent) {
            // Logik-Block: Permanente Items werden ignoriert
            return
        }
        viewModelScope.launch {
            watchlistRepo.removeStock(stock)
        }
    }
}