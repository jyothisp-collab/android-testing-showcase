package com.example.androidtestingshowcase.features.home

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = HomeViewModel()
    }

    @Test
    fun `initial state has default message`() = runTest {
        assertEquals("Tests verify behavior at every layer.", viewModel.uiState.value.message)
    }

    @Test
    fun `refresh updates message with timestamp`() = runTest {
        viewModel.uiState.test {
            awaitItem().let { assertEquals("Tests verify behavior at every layer.", it.message) }
            viewModel.refresh()
            advanceUntilIdle()
            val updated = awaitItem()
            assertEquals("Action completed at ${System.currentTimeMillis()}", updated.message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
