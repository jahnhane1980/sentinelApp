package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Sentinel Market Score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        // Hier wird dein Score angezeigt!
        Text(
            text = "Score: ${"%.1f".format(viewModel.currentScore)} / 10",
            fontSize = 48.sp,
            color = if (viewModel.currentScore < 5.0) Color.Red else Color.Green
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { viewModel.updateAnalysis() }) {
            Text("Update Analysis")
        }
    }
}