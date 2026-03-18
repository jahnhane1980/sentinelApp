package com.sentinel.deeptrace.config

import com.sentinel.deeptrace.ui.theme.SentinelBlue

object AppConfig {
    // --- Allgemeine App-Einstellungen ---
    const val APP_NAME = "Sentinel Deep Trace"
    const val DEFAULT_LOCALE = "de"
    const val REFRESH_INTERVAL_MS = 30000L

    // --- KI / Google Gemini ---
    const val GEMINI_MODEL = "gemini-1.5-flash"
    const val MAX_DAILY_AI_CALLS = 50

    // --- Schwellenwerte für Scores (10-Punkt System) ---
    object Thresholds {
        const val SCORE_SQUEEZE = 8.5
        const val SCORE_HIGH = 7.5
        const val SCORE_MEDIUM = 4.0
    }

    // --- Markt-Hypothese & Risiko-Parameter (Deine Working Hypothesis) ---
    object MarketRisks {
        const val USD_JPY_CRITICAL_LEVEL = 150.0
        const val SP500_BLOW_OFF_TOP_TARGET = 7500.0
        const val VIX_HEDGE_CALL_STRIKE = 30.0
        const val VIX_SQUEEZE_ALERT = 25.0
    }

    object UserPreferences {
        const val DEFAULT_CURRENCY = "EUR"
        const val SHOW_EMPTY_PORTFOLIO_MESSAGE = true
    }

    // --- UI Defaults ---
    val DEFAULT_CHART_COLOR = SentinelBlue
    const val HAPTIC_FEEDBACK_ENABLED = true
}