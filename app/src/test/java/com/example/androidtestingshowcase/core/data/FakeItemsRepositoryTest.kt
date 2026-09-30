package com.example.androidtestingshowcase.core.data

import app.cash.turbine.test
import com.example.androidtestingshowcase.core.common.Result
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
class FakeItemsRepositoryTest {

    private lateinit var repository: FakeItemsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeItemsRepository()
    }

    @Test
    fun `observeItems returns expected items`() = runTest {
        repository.observeItems().test {
            val items = awaitItem()
            assertEquals(0, items.size)
            assertEquals(true, items.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `observeItems reflects items added via setItems`() = runTest {
        val testItems = listOf(
            ShowcaseItem("1", "First", "Detail one", 1L),
            ShowcaseItem("2", "Second", "Detail two", 2L),
        )

        repository.setItems(testItems)

        repository.observeItems().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("First", items[0].title)
            assertEquals("Second", items[1].title)
            awaitComplete()
        }
    }

    @Test
    fun `refreshItems returns Success`() = runTest {
        repository.setItems(
            listOf(ShowcaseItem("1", "One", "Detail", 1L))
        )

        val result = repository.refreshItems()
        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
    }

    @Test
    fun `addItem adds item to the flow`() = runTest {
        val item = ShowcaseItem("1", "New", "New detail", 1L)
        repository.addItem(item)

        repository.observeItems().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("New", items[0].title)
            awaitComplete()
        }
    }

    @Test
    fun `clear removes all items`() = runTest {
        repository.setItems(
            listOf(
                ShowcaseItem("1", "One", "Detail", 1L),
                ShowcaseItem("2", "Two", "Detail", 2L),
            )
        )
        repository.clear()

        repository.observeItems().test {
            val items = awaitItem()
            assertEquals(0, items.size)
            assertTrue(items.isEmpty())
            awaitComplete()
        }
    }
}
