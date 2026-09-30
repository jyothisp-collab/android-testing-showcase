package com.example.androidtestingshowcase.core.data

import app.cash.turbine.test
import com.example.androidtestingshowcase.core.common.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `observeItems returns expected list`() = runTest {
        repository.observeItems().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("One", items[0].title)
            assertEquals("Two", items[1].title)
            awaitComplete()
        }
    }

    @Test
    fun `refresh returns Success`() = runTest {
        val result = repository.refresh()
        assert(result is Success)
    }
}
