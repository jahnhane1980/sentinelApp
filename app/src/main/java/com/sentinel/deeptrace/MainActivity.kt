package com.sentinel.deeptrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.deeptrace.data.db.SentinelDatabase
import com.sentinel.deeptrace.data.repository.LocalWatchlistRepository
import com.sentinel.deeptrace.data.pref.SettingsRepository
import com.sentinel.deeptrace.data.remote.GoogleAiRepository
import com.sentinel.deeptrace.data.repository.AkrominaResearchRepositoryImpl
import com.sentinel.deeptrace.data.repository.WatchlistRepository
import com.sentinel.deeptrace.data.repository.AkrominaResearchRepository
import com.sentinel.deeptrace.data.AiRepository
import com.sentinel.deeptrace.ui.dashboard.*
import com.sentinel.deeptrace.ui.theme.SentinelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Datenbank-Instanz holen
        val database = SentinelDatabase.getDatabase(this)

        // 2. Repositories initialisieren
        // Wir nutzen die Interfaces als Typen (links), um Flexibilität zu wahren
        val watchlistRepo: WatchlistRepository = LocalWatchlistRepository(database.watchlistDao())
        val settingsRepo: SettingsRepository = SettingsRepository(this)
        val aiRepo: AiRepository = GoogleAiRepository(settingsRepo)
        val akrominaRepo: AkrominaResearchRepository = AkrominaResearchRepositoryImpl()

        // 3. Factory mit allen 4 benötigten Repositories erstellen
        // WICHTIG: Die Reihenfolge muss exakt der im SentinelViewModelFactory-Konstruktor entsprechen
        val factory = SentinelViewModelFactory(
            watchlistRepository = watchlistRepo,
            settingsRepository = settingsRepo,
            aiRepository = aiRepo,
            akrominaRepository = akrominaRepo
        )

        setContent {
            SentinelTheme {
                // 4. ViewModel über die Factory injizieren
                val viewModel: SentinelViewModel = viewModel(factory = factory)

                // 5. Haupt-Screen aufrufen
                SentinelScreen(viewModel = viewModel)
            }
        }
    }
}