package com.example.androidtestingshowcase.features.home

import androidx.lifecycle.ViewModel
import com.example.androidtestingshowcase.core.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeUiState(val message: String = "Tests verify behavior at every layer.")

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.update { it.copy(message = "Action completed at ${System.currentTimeMillis()}") }
    }
}
