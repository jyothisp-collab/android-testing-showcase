package com.example.androidtestingshowcase.core.network

import com.example.androidtestingshowcase.core.network.model.ShowcaseItemResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ShowcaseApi {
    @GET("items")
    suspend fun getItems(): List<ShowcaseItemResponse>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: String): ShowcaseItemResponse
}
