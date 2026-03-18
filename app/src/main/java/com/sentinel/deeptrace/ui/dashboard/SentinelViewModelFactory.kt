package com.sentinel.deeptrace.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sentinel.deeptrace.data.repository.SettingsRepository
import com.sentinel.deeptrace.data.repository.WatchlistRepository

class SentinelViewModelFactory(
    private val watchlistRepository: WatchlistRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SentinelViewModel::class.java)) {
            return SentinelViewModel(watchlistRepository, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}