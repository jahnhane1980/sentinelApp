package com.sentinel.deeptrace.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "sentinel_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
    }

    // Lesen der Währung (mit Fallback auf "EUR")
    val defaultCurrency: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.DEFAULT_CURRENCY] ?: "EUR"
    }

    // Lesen der Haptik
    val hapticEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.HAPTIC_ENABLED] ?: true
    }

    // Speichern
    suspend fun updateCurrency(currency: String) {
        context.dataStore.edit { prefs -> prefs[Keys.DEFAULT_CURRENCY] = currency }
    }

    suspend fun updateHaptic(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.HAPTIC_ENABLED] = enabled }
    }
}