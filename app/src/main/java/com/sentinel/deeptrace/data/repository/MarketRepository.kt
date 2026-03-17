package com.sentinel.deeptrace.data.repository

import com.sentinel.deeptrace.data.model.MarketData

interface MarketRepository {
    suspend fun getMacroData(): MarketData
}