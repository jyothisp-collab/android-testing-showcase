package com.example.androidtestingshowcase.core.data

import com.example.androidtestingshowcase.core.common.Result
import com.example.androidtestingshowcase.core.database.ShowcaseItemDao
import com.example.androidtestingshowcase.core.database.asDomain
import com.example.androidtestingshowcase.core.database.asEntity
import com.example.androidtestingshowcase.core.network.ShowcaseApi
import com.example.androidtestingshowcase.core.network.asDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor(
    private val api: ShowcaseApi,
    private val dao: ShowcaseItemDao,
) {
    fun observeItems(): Flow<List<ShowcaseItem>> =
        dao.observeItems().map { entities -> entities.map { it.asDomain() } }

    suspend fun refreshItems(): Result<List<ShowcaseItem>> {
        return runCatching {
            val remoteItems = api.getItems().map { it.asDomain() }
            dao.upsertAll(remoteItems.map { it.asEntity() })
            remoteItems
        }.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { Result.Error("Could not refresh items", it) },
        )
    }
}
