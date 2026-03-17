@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("SENTINEL SCORE: ${viewModel.currentScore}", style = MaterialTheme.typography.headlineLarge)
        
        Button(onClick = { viewModel.toggleFrequency() }) {
            Text("Intervall: ${viewModel.frequency}")
        }
        
        Button(onClick = { viewModel.updateAnalysis() }) {
            Text("Refresh Data")
        }
    }
}