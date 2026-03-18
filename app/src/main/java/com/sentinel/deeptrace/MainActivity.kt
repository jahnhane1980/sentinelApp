package com.sentinel.deeptrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.repository.LocalWatchlistRepository
import com.sentinel.deeptrace.data.repository.SettingsRepository
import com.sentinel.deeptrace.ui.dashboard.SentinelScreen
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModelFactory
import com.sentinel.deeptrace.ui.theme.SentinelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Komponenten initialisieren
        val database = SentinelDatabase.getDatabase(this)
        val watchlistRepo = LocalWatchlistRepository(database.watchlistDao())
        val settingsRepo = SettingsRepository(this)

        // 2. Factory erstellen
        val factory = SentinelViewModelFactory(watchlistRepo, settingsRepo)

        setContent {
            SentinelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 3. ViewModel über die Factory anfordern
                    val viewModel: SentinelViewModel = viewModel(factory = factory)

                    SentinelScreen(viewModel = viewModel)
                }
            }
        }
    }
}