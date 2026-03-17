package com.sentinel.deeptrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.deeptrace.ui.dashboard.SentinelScreen
import com.sentinel.deeptrace.ui.dashboard.SentinelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Wir nutzen MaterialTheme als Basis, solange wir kein eigenes Theme-File haben
            MaterialTheme {
                // Ein Surface Container, der den Hintergrund des Handys füllt
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Hier wird das ViewModel erzeugt
                    val viewModel: SentinelViewModel = viewModel()

                    // Dein Dashboard wird aufgerufen und bekommt das ViewModel übergeben
                    SentinelScreen(viewModel = viewModel)
                }
            }
        }
    }
}