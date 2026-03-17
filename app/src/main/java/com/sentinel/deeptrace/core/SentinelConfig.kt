package com.sentinel.deeptrace.core

enum class ObservationFrequency { WEEKLY, FOUR_HOURS }

object SentinelConfig {
    const val USE_MOCK_DATA = true // GLOBALER SCHALTER
    var currentFrequency = ObservationFrequency.WEEKLY
}