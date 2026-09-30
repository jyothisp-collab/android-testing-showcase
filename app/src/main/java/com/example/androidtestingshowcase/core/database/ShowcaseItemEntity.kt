package com.example.androidtestingshowcase.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtestingshowcase.core.data.ShowcaseItem

@Entity(tableName = "showcase_items")
data class ShowcaseItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val detail: String,
    val updatedAtMillis: Long,
)

fun ShowcaseItemEntity.asDomain() = ShowcaseItem(
    id = id,
    title = title,
    detail = detail,
    updatedAtMillis = updatedAtMillis,
)

fun ShowcaseItem.asEntity() = ShowcaseItemEntity(
    id = id,
    title = title,
    detail = detail,
    updatedAtMillis = updatedAtMillis,
)
