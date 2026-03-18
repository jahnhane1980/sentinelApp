package com.sentinel.deeptrace.data.pref

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

private val Context.dataStore by preferencesDataStore(name = "sentinel_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val GOOGLE_API_KEY = stringPreferencesKey("google_api_key")
        val AI_CALL_COUNT = intPreferencesKey("ai_call_count")
        val LAST_RESET_DATE = longPreferencesKey("last_reset_date")
    }

    val defaultCurrency: Flow<String> = context.dataStore.data.map { it[Keys.DEFAULT_CURRENCY] ?: "EUR" }
    val hapticEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.HAPTIC_ENABLED] ?: true }
    val googleApiKey: Flow<String> = context.dataStore.data.map { it[Keys.GOOGLE_API_KEY] ?: "" }
    val aiCallCount: Flow<Int> = context.dataStore.data.map { checkAndResetCounter(it) }

    suspend fun updateCurrency(currency: String) { context.dataStore.edit { it[Keys.DEFAULT_CURRENCY] = currency } }
    suspend fun updateHaptic(enabled: Boolean) { context.dataStore.edit { it[Keys.HAPTIC_ENABLED] = enabled } }
    suspend fun updateGoogleApiKey(key: String) { context.dataStore.edit { it[Keys.GOOGLE_API_KEY] = key } }

    suspend fun incrementAiCounter() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.AI_CALL_COUNT] ?: 0
            prefs[Keys.AI_CALL_COUNT] = current + 1
        }
    }

    private fun checkAndResetCounter(prefs: Preferences): Int {
        val lastReset = prefs[Keys.LAST_RESET_DATE] ?: 0L
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return if (today > lastReset) 0 else prefs[Keys.AI_CALL_COUNT] ?: 0
    }

    suspend fun resetAiCounterIfNecessary() {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        context.dataStore.edit { prefs ->
            val lastReset = prefs[Keys.LAST_RESET_DATE] ?: 0L
            if (today > lastReset) {
                prefs[Keys.AI_CALL_COUNT] = 0
                prefs[Keys.LAST_RESET_DATE] = today
            }
        }
    }
}