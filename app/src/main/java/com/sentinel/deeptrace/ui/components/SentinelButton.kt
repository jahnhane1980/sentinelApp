package com.sentinel.deeptrace.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sentinel.deeptrace.ui.theme.SentinelBlue
import com.sentinel.deeptrace.ui.theme.SentinelDimens // WICHTIGER IMPORT

@Composable
fun SentinelPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(SentinelDimens.ButtonHeight),
        colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}