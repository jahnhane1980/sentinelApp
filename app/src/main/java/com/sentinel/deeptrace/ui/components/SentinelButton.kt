package com.sentinel.deeptrace.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sentinel.deeptrace.ui.theme.SentinelBlue

@Composable
fun SentinelPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SentinelBlue),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, color = Color.White)
    }
}