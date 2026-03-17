package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "SENTINEL SCORE: ${String.format("%.1f", viewModel.currentScore)}",
            style = MaterialTheme.typography.headlineLarge
        )

        Button(onClick = { viewModel.toggleFrequency() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Intervall: ${viewModel.frequency}")
        }

        Button(onClick = { viewModel.updateAnalysis() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Refresh Data")
        }
    }
}