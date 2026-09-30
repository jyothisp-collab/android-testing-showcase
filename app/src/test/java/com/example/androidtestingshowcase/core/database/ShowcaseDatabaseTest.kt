package com.example.androidtestingshowcase.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShowcaseDatabaseTest {

    private lateinit var database: ShowcaseDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShowcaseDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insert items and query them back`() = runTest {
        val items = listOf(
            ShowcaseItemEntity(id = "1", title = "First", detail = "First detail", updatedAtMillis = 1L),
            ShowcaseItemEntity(id = "2", title = "Second", detail = "Second detail", updatedAtMillis = 2L),
        )

        database.itemDao().upsertAll(items)

        val allItems = database.itemDao().observeItems()
        allItems.test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("First", result[0].title)
            assertEquals("Second", result[1].title)
            assertEquals(2L, result[1].updatedAtMillis)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `upsert replaces existing item`() = runTest {
        val initial = ShowcaseItemEntity(id = "1", title = "Original", detail = "Original detail", updatedAtMillis = 1L)
        val updated = ShowcaseItemEntity(id = "1", title = "Updated", detail = "Updated detail", updatedAtMillis = 2L)

        database.itemDao().upsertAll(listOf(initial))
        database.itemDao().upsertAll(listOf(updated))

        val allItems = database.itemDao().observeItems()
        allItems.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Updated", result[0].title)
            assertEquals("Updated detail", result[0].detail)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clear removes all items`() = runTest {
        database.itemDao().upsertAll(
            listOf(
                ShowcaseItemEntity(id = "1", title = "A", detail = "A detail", updatedAtMillis = 1L),
                ShowcaseItemEntity(id = "2", title = "B", detail = "B detail", updatedAtMillis = 2L),
            )
        )

        database.itemDao().clear()

        val allItems = database.itemDao().observeItems()
        allItems.test {
            val result = awaitItem()
            assertEquals(0, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
