package com.example.androidtestingshowcase.features.home

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: com.example.androidtestingshowcase.core.data.ItemsRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = createTestRepository()
        viewModel = HomeViewModel.createWithRepository(repository)
    }

    @Test
    fun `initial state has empty items and loading`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(true, state.isLoading)
        assertEquals(0, state.items.size)
        assertEquals(null, state.errorMessage)
    }

    @Test
    fun `items loaded after initialization`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(true, initialState.isLoading)

            val loadedState = awaitItem()
            assertEquals(false, loadedState.isLoading)
            assertEquals(2, loadedState.items.size)
            assertEquals("Item One", loadedState.items[0].title)
            assertEquals("Item Two", loadedState.items[1].title)
            assertEquals(null, loadedState.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh triggers reload`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            val loadedState = awaitItem()
            assertEquals(false, loadedState.isLoading)

            viewModel.refresh()
            val refreshedState = awaitItem()
            assertEquals(true, refreshedState.isLoading)

            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(2, finalState.items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error state is set on failure`() = runTest {
        val failingRepo = object : com.example.androidtestingshowcase.core.data.ItemsRepository {
            override fun observeItems() =
                kotlinx.coroutines.flow.flowOf(emptyList<com.example.androidtestingshowcase.core.data.ShowcaseItem>())

            override suspend fun refreshItems(): com.example.androidtestingshowcase.core.common.Result<List<com.example.androidtestingshowcase.core.data.ShowcaseItem>> {
                return com.example.androidtestingshowcase.core.common.Result.Error("Network failure")
            }
        }

        val errorViewModel = HomeViewModel.createWithRepository(failingRepo)

        errorViewModel.uiState.test {
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals("Network failure", errorState.errorMessage)
            assertEquals(0, errorState.items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createTestRepository(): com.example.androidtestingshowcase.core.data.ItemsRepository {
        return object : com.example.androidtestingshowcase.core.data.ItemsRepository {
            private val items = kotlinx.coroutines.flow.MutableStateFlow(
                listOf(
                    com.example.androidtestingshowcase.core.data.ShowcaseItem(
                        "1", "Item One", "First item detail", 1_600_000_000_000L,
                    ),
                    com.example.androidtestingshowcase.core.data.ShowcaseItem(
                        "2", "Item Two", "Second item detail", 1_600_000_100_000L,
                    ),
                )
            )

            override fun observeItems() = items.asStateFlow()

            override suspend fun refreshItems(): com.example.androidtestingshowcase.core.common.Result<List<com.example.androidtestingshowcase.core.data.ShowcaseItem>> {
                return com.example.androidtestingshowcase.core.common.Result.Success(items.value)
            }
        }
    }
}
