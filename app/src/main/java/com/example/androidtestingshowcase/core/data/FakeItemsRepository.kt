package com.example.androidtestingshowcase.core.data

import com.example.androidtestingshowcase.core.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ItemsRepository {
    fun observeItems(): Flow<List<ShowcaseItem>>
    suspend fun refreshItems(): Result<List<ShowcaseItem>>
}

class FakeItemsRepository(
    private val initialItems: List<ShowcaseItem> = emptyList(),
) : ItemsRepository {

    private val _items = MutableStateFlow(initialItems)
    val itemsFlow: Flow<List<ShowcaseItem>> = _items.asStateFlow()

    override fun observeItems(): Flow<List<ShowcaseItem>> = _items.asStateFlow()

    override suspend fun refreshItems(): Result<List<ShowcaseItem>> {
        return Result.Success(_items.value)
    }

    fun setItems(items: List<ShowcaseItem>) {
        _items.value = items
    }

    fun addItem(item: ShowcaseItem) {
        _items.value = _items.value + item
    }

    fun clear() {
        _items.value = emptyList()
    }
}
