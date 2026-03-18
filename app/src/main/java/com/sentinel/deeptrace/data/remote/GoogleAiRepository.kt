package com.sentinel.deeptrace.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.sentinel.deeptrace.config.AppConfig
import com.sentinel.deeptrace.data.AiRepository
import com.sentinel.deeptrace.data.pref.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GoogleAiRepository(private val settingsRepository: SettingsRepository) : AiRepository {

    override suspend fun getRelevantNews(context: String): Result<String> {
        val key = settingsRepository.googleApiKey.first()
        if (key.isBlank()) return Result.failure(Exception("Kein API Key hinterlegt"))

        val count = settingsRepository.aiCallCount.first()
        if (count >= AppConfig.MAX_DAILY_AI_CALLS) return Result.failure(Exception("Tageslimit erreicht"))

        return try {
            val model = GenerativeModel(modelName = AppConfig.GEMINI_MODEL, apiKey = key)
            val response = model.generateContent("Analysiere als neutraler Analyst: $context")
            settingsRepository.incrementAiCounter()
            Result.success(response.text ?: "Keine Analyse verfügbar")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun canMakeCall(): Flow<Boolean> = settingsRepository.aiCallCount.map {
        it < AppConfig.MAX_DAILY_AI_CALLS
    }
}