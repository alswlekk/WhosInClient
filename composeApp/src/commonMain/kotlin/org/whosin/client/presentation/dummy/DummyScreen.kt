package org.whosin.client.presentation.dummy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DummyScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: DummyViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = uiState) {
            is DummyUiState.Loading -> CircularProgressIndicator()
            is DummyUiState.Success -> {
                Text(text = state.joke.setup)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = state.joke.punchline)
                Spacer(modifier = Modifier.size(20.dp))
            }
            is DummyUiState.Error -> Text(text = state.message ?: "Unknown error")
        }

        Button(onClick = { viewModel.getJoke() }) {
            Text("Reload")
        }
    }
}