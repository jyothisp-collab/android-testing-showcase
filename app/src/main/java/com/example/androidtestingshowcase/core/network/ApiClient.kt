package com.example.androidtestingshowcase.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
object ApiClient private constructor(
    private val baseUrl: String,
    private val client: OkHttpClient,
) {
    fun create(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    class Factory @Inject constructor(
        private val okHttpClient: OkHttpClient,
    ) {
        fun create(baseUrl: String): Retrofit = ApiClient(baseUrl, okHttpClient).create()
    }
}
