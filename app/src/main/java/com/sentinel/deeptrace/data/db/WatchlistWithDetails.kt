package com.sentinel.deeptrace.data.db

/**
 * UI-Modell für die Watchlist inkl. Portfolio-Berechnungen.
 * Diese Klasse ist keine @Entity, sondern ein POJO (Plain Old Java Object),
 * das die Ergebnisse der SQL-Joins im DAO aufnimmt.
 */
data class WatchlistWithDetails(
    val symbol: String,
    val name: String,
    val marketName: String,
    val score: Double,
    val isPermanent: Boolean,
    val totalHoldings: Double = 0.0,
    val totalInvested: Double = 0.0,
    val currency: String? = null
)