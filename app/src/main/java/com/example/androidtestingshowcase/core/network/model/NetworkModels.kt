package com.example.androidtestingshowcase.core.network.model

import com.example.androidtestingshowcase.core.data.ShowcaseItem
import com.google.gson.annotations.SerializedName

data class ShowcaseItemResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("updatedAtMillis") val updatedAtMillis: Long,
)

fun ShowcaseItemResponse.asDomain() = ShowcaseItem(
    id = id,
    title = title,
    detail = detail,
    updatedAtMillis = updatedAtMillis,
)
