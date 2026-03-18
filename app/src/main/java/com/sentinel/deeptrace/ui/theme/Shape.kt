package com.sentinel.deeptrace.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Wir behalten den Namen SentinelShapes bei, falls er so in deinem Theme referenziert wird
val SentinelShapes = Shapes(
    // small wird für Buttons und Textfelder genutzt.
    // 4.dp sorgt für den gewünschten "technischen/eckigen" Look.
    small = RoundedCornerShape(4.dp),

    // medium wird meist für die Cards in der Watchlist genutzt.
    medium = RoundedCornerShape(12.dp),

    // large wird für die Dialog-Hintergründe (Add/Edit) genutzt.
    large = RoundedCornerShape(16.dp),

    // extraLarge bleibt erhalten für sehr große Oberflächen oder Sheets.
    extraLarge = RoundedCornerShape(24.dp)
)