package com.example.androidtestingshowcase.features.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidtestingshowcase.core.data.ShowcaseItem

@Composable
fun ItemDetailRoute(
    onBack: () -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ItemDetailScreen(state = state, onBack = onBack, onRefresh = viewModel::refresh)
}

@Composable
fun ItemDetailScreen(
    state: ItemDetailUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        TextButton(onClick = onBack) { Text("Back") }
        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.isLoading -> CircularProgressIndicator()
            state.item != null -> {
                Text(state.item.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(12.dp))
                Text(state.item.detail)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onRefresh) { Text("Refresh detail") }
            }
            else -> Text(state.errorMessage ?: "Item not found")
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
