package com.example.androidtestingshowcase.features.home

import com.example.androidtestingshowcase.core.common.Result
import com.example.androidtestingshowcase.core.data.ShowcaseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ItemsRepository {
    fun observeItems(): Flow<List<ShowcaseItem>>
    suspend fun refresh(): Result<Unit>
}

class FakeItemsRepository : ItemsRepository {
    private val items = listOf(
        ShowcaseItem("1", "One", "First item", 1L),
        ShowcaseItem("2", "Two", "Second item", 2L),
    )

    override fun observeItems(): Flow<List<ShowcaseItem>> = flowOf(items)
    override suspend fun refresh(): Result<Unit> = Result.Success(Unit)
}
