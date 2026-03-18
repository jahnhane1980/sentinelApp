package com.sentinel.deeptrace.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface AkrominaResearchRepository {
    fun getResearchData(): Flow<String>
}

class AkrominaResearchRepositoryImpl : AkrominaResearchRepository {
    override fun getResearchData(): Flow<String> = flowOf("Akromina Research System bereit.")
}