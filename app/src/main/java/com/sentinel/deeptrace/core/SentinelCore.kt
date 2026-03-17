package com.sentinel.deeptrace.core

object SentinelCore {
    // Hier müssen EXAKT diese Namen in den Klammern stehen:
    fun calculateScore(
        usdJpy: Double,
        fedRepoFlow: Double,
        vix: Double,        // <--- Wenn das hier fehlt, kommt dein Fehler!
        sp500: Double,
        gold: Double
    ): Double {
        var score = 10.0

        // Beispiel-Logik für deine Hypothese:
        if (usdJpy < 150.0) score -= 3.0
        if (vix > 20.0) score -= 2.0
        if (fedRepoFlow < 600.0) score -= 1.0

        return score.coerceIn(0.0, 10.0)
    }
}