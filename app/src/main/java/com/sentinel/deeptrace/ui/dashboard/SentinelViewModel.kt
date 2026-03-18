package com.sentinel.deeptrace.ui.dashboard

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.*
import com.sentinel.deeptrace.data.repository.*
import com.sentinel.deeptrace.domain.GetSentinelScoreUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SentinelViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SentinelDatabase.getDatabase(application)
    private val watchlistRepo = LocalWatchlistRepository(db.watchlistDao())

    // Annahme: Dein UseCase für die Markt-Analyse
    private val getSentinelScoreUseCase = GetSentinelScoreUseCase(FakeMarketRepository())

    // --- UI State ---
    var marketData by mutableStateOf<MarketData?>(null)
    var uiErrorMessage by mutableStateOf<String?>(null)

    // Watchlist-Stream: Dank des SQL-Joins im DAO kommen hier
    // die summierten Holdings & Invested Capital direkt an.
    val watchlist = watchlistRepo.getWatchlist().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Währungen dynamisch aus der Markets-Tabelle laden
    val availableCurrencies = flow {
        emit(db.watchlistDao().getAllMarketCurrencies().distinct())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf("EUR", "USD"))

    init {
        updateAnalysis()
    }

    // --- Kern-Funktionen ---

    fun updateAnalysis() {
        viewModelScope.launch {
            marketData = getSentinelScoreUseCase()
        }
    }

    fun clearError() {
        uiErrorMessage = null
    }

    suspend fun searchMasterAssets(query: String): List<AssetMaster> {
        return watchlistRepo.searchMasterAssets(query)
    }

    /**
     * Fügt ein Asset zur Watchlist hinzu.
     */
    fun addStockWithValidation(symbol: String, name: String) {
        val app = getApplication<Application>()
        viewModelScope.launch {
            val exists = watchlistRepo.exists(symbol)
            if (exists) {
                uiErrorMessage = app.getString(R.string.error_already_in_watchlist, symbol)
            } else {
                watchlistRepo.addStock(WatchlistItem(symbol = symbol))
            }
        }
    }

    /**
     * Bucht eine neue Transaktion (Kauf oder Verkauf).
     * Das Repository schreibt in die 'transactions' Tabelle.
     */
    fun bookTransaction(symbol: String, deltaHoldings: Double, deltaCapital: Double, currency: String) {
        val app = getApplication<Application>()
        viewModelScope.launch {
            // 1. Konsistenz-Check: Hat das Asset bereits eine fixierte Währung?
            // Wir prüfen das direkt über das nackte Watchlist-Objekt (Internal)
            val currentItem = watchlistRepo.getWatchlistItemInternal(symbol)

            // Falls das Asset bereits Transaktionen hat, ziehen wir uns die Währung
            // der ersten Transaktion zur Validierung.
            val fixatedCurrency = watchlist.value.find { it.symbol == symbol }?.currency

            if (fixatedCurrency != null && fixatedCurrency != currency) {
                uiErrorMessage = app.getString(R.string.error_currency_mismatch, fixatedCurrency)
                return@launch
            }

            // 2. Transaktion erstellen
            val newTransaction = Transaction(
                symbol = symbol,
                amount = deltaHoldings,
                totalPrice = deltaCapital,
                currency = currency
            )

            // 3. In DB speichern
            watchlistRepo.addTransaction(newTransaction)

            // HINWEIS: Da 'watchlist' ein Flow ist, der auf der transactions-Tabelle joint,
            // aktualisiert sich die UI jetzt vollautomatisch!
        }
    }

    fun removeStock(symbol: String) {
        viewModelScope.launch {
            watchlistRepo.removeStock(symbol)
        }
    }
}