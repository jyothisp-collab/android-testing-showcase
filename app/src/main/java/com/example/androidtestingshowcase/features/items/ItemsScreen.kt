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
fun ItemsRoute(
    onBack: () -> Unit,
    onOpenDetail: (String) -> Unit,
    viewModel: ItemsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ItemsScreen(state = state, onBack = onBack, onOpenDetail = onOpenDetail)
}

@Composable
fun ItemsScreen(
    state: ItemsUiState,
    onBack: () -> Unit,
    onOpenDetail: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextButton(onClick = onBack) { Text("Back") }
        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.isLoading -> CircularProgressIndicator()
            state.items.isNotEmpty() -> {
                state.items.forEach { item ->
                    Button(onClick = { onOpenDetail(item.id) }) {
                        Text(item.title)
                    }
                }
            }
            else -> Text(state.errorMessage ?: "No items found")
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

data class ItemsUiState(
    val items: List<ShowcaseItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
