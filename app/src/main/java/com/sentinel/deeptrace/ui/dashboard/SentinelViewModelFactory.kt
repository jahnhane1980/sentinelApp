package com.sentinel.deeptrace.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sentinel.deeptrace.data.AiRepository
import com.sentinel.deeptrace.data.repository.*
import com.sentinel.deeptrace.data.pref.SettingsRepository

class SentinelViewModelFactory(
    private val watchlistRepository: WatchlistRepository,
    private val settingsRepository: SettingsRepository,
    private val aiRepository: AiRepository,
    private val akrominaRepository: AkrominaResearchRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SentinelViewModel(watchlistRepository, settingsRepository, aiRepository, akrominaRepository) as T
    }
}