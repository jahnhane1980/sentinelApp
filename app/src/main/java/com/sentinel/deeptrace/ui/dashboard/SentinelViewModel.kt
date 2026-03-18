package com.sentinel.deeptrace.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.deeptrace.data.db.WatchlistWithDetails
import com.sentinel.deeptrace.data.model.AssetMaster
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.data.repository.WatchlistRepository
import com.sentinel.deeptrace.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SentinelViewModel(
    private val watchlistRepository: WatchlistRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val marketData: MarketData? = watchlistRepository.getMockMarketData()

    val watchlist: StateFlow<List<WatchlistWithDetails>> = watchlistRepository.getWatchlistWithDetails()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userCurrency: StateFlow<String> = settingsRepository.defaultCurrency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "EUR")

    val isHapticActive: StateFlow<Boolean> = settingsRepository.hapticEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val availableCurrencies = watchlistRepository.getAvailableCurrencies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("EUR", "USD"))

    // --- NEU: Suchlogik ---
    private val _searchResults = MutableStateFlow<List<AssetMaster>>(emptyList())
    val searchResults: StateFlow<List<AssetMaster>> = _searchResults.asStateFlow()

    fun searchMasterAssets(query: String) {
        viewModelScope.launch {
            if (query.length >= 2) {
                _searchResults.value = watchlistRepository.searchAssets(query)
            } else {
                _searchResults.value = emptyList()
            }
        }
    }

    // --- Aktionen ---
    fun addStockWithValidation(symbol: String, name: String) {
        viewModelScope.launch { watchlistRepository.addAsset(symbol, name) }
    }

    fun removeStock(symbol: String) {
        viewModelScope.launch { watchlistRepository.deleteAsset(symbol) }
    }

    fun bookTransaction(symbol: String, amount: Double, price: Double, currency: String) {
        viewModelScope.launch { watchlistRepository.addTransaction(symbol, amount, price, currency) }
    }

    fun updateDefaultCurrency(currency: String) {
        viewModelScope.launch { settingsRepository.updateCurrency(currency) }
    }

    fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateHaptic(enabled) }
    }
}