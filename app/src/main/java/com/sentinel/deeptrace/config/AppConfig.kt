package com.sentinel.deeptrace.config

import androidx.compose.ui.graphics.Color
import com.sentinel.deeptrace.ui.theme.SentinelBlue
import com.sentinel.deeptrace.ui.theme.SentinelOrange
import com.sentinel.deeptrace.ui.theme.SentinelRed

object AppConfig {
    // --- Allgemeine App-Einstellungen ---
    const val APP_NAME = "Sentinel Deep Trace"
    const val DEFAULT_LOCALE = "de"
    const val REFRESH_INTERVAL_MS = 30000L // 30 Sekunden für Live-Updates

    // --- Schwellenwerte für Scores (10-Punkt System) ---
    object Thresholds {
        const val SCORE_HIGH = 7.5    // Ab hier SentinelBlue (Stabil/Bullish)
        const val SCORE_MEDIUM = 4.0  // Ab hier SentinelOrange (Neutral/Warnung)
        // Alles darunter ist SentinelRed (Kritisch)
    }

    // --- Markt-Hypothese & Risiko-Parameter ---
    object MarketRisks {
        const val USD_JPY_CRITICAL_LEVEL = 150.0 // Yen-Carry Trade Trigger
        const val SP500_BLOW_OFF_TOP_TARGET = 7500.0
        const val VIX_HEDGE_CALL_STRIKE = 30.0
    }

    // --- UI Defaults ---
    val DEFAULT_CHART_COLOR = SentinelBlue
    const val HAPTIC_FEEDBACK_ENABLED = true
}