package com.example.androidtestingshowcase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShowcaseItemTest {

    @Test
    fun `data class equality`() {
        val item1 = ShowcaseItem("1", "Title", "Detail", 1000L)
        val item2 = ShowcaseItem("1", "Title", "Detail", 1000L)
        val item3 = ShowcaseItem("2", "Other", "Other", 2000L)

        assertEquals(item1, item2)
        assertEquals(item1.hashCode(), item2.hashCode())
        assertTrue(item1 == item2)
        assertFalse(item1 == item3)
    }

    @Test
    fun `copy preserves unchanged fields`() {
        val original = ShowcaseItem("1", "Title", "Detail", 1000L)
        val copy = original.copy(title = "New Title")

        assertEquals("1", copy.id)
        assertEquals("New Title", copy.title)
        assertEquals("Detail", copy.detail)
        assertEquals(1000L, copy.updatedAtMillis)
    }

    @Test
    fun `default values are correct`() {
        val item = ShowcaseItem(
            id = "",
            title = "",
            detail = "",
            updatedAtMillis = 0L,
        )

        assertEquals("", item.id)
        assertEquals("", item.title)
        assertEquals("", item.detail)
        assertEquals(0L, item.updatedAtMillis)
    }

    @Test
    fun `toString contains all fields`() {
        val item = ShowcaseItem("id-1", "My Title", "My Detail", 5000L)
        val str = item.toString()

        assertTrue(str.contains("id-1"))
        assertTrue(str.contains("My Title"))
        assertTrue(str.contains("My Detail"))
        assertTrue(str.contains("5000"))
    }
}
