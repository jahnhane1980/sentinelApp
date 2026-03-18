package com.sentinel.deeptrace.data

import kotlinx.coroutines.flow.Flow

interface AiRepository {
    suspend fun getRelevantNews(context: String): Result<String>
    fun canMakeCall(): Flow<Boolean>
}