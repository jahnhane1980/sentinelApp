package com.sentinel.deeptrace.ui.dashboard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.model.WatchlistItem
import com.sentinel.deeptrace.data.model.MarketData
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

    // Hält die Makro-Daten (VIX, Repo, etc.)
    var marketData by mutableStateOf<MarketData?>(null)
        private set

    // State für Fehlermeldungen (z.B. Ticker existiert bereits)
    var uiErrorMessage by mutableStateOf<String?>(null)
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
            // Holt die Daten aus dem Fake-Repository über den UseCase
            marketData = getSentinelScoreUseCase()
        }
    }

    fun clearError() {
        uiErrorMessage = null
    }

    fun addStock(symbol: String, name: String) {
        val cleanSymbol = symbol.uppercase().trim()
        if (cleanSymbol.isEmpty()) return

        viewModelScope.launch {
            if (watchlistRepo.exists(cleanSymbol)) {
                uiErrorMessage = "Ticker $cleanSymbol existiert bereits!"
            } else {
                watchlistRepo.addStock(WatchlistItem(symbol = cleanSymbol, name = name))
            }
        }
    }

    fun updateStock(stock: WatchlistItem, newName: String, newSymbol: String) {
        val cleanSymbol = newSymbol.uppercase().trim()
        viewModelScope.launch {
            if (stock.symbol == cleanSymbol) {
                watchlistRepo.updateStock(stock.copy(name = newName))
            } else {
                if (watchlistRepo.exists(cleanSymbol)) {
                    uiErrorMessage = "Symbol $cleanSymbol wird bereits verwendet!"
                } else {
                    watchlistRepo.removeStock(stock)
                    watchlistRepo.addStock(WatchlistItem(symbol = cleanSymbol, name = newName))
                }
            }
        }
    }

    fun removeStock(stock: WatchlistItem) {
        if (!stock.isPermanent) {
            viewModelScope.launch {
                watchlistRepo.removeStock(stock)
            }
        }
    }
}