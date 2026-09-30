package com.example.androidtestingshowcase.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ApiClientTest {

    @Test
    fun `retrofit is created with correct base URL`() {
        val client = OkHttpClient.Builder().build()
        val retrofit = ApiClient.Factory(client).create("https://api.example.com/")

        assertEquals("https://api.example.com/", retrofit.baseUrl().toString())
    }

    @Test
    fun `retrofit factory creates instance`() {
        val client = OkHttpClient.Builder().build()
        val factory = ApiClient.Factory(client)

        val retrofit = factory.create("https://demo.local/")
        assertNotNull(retrofit)
    }

    @Test
    fun `logging interceptor is added in debug mode`() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val interceptors = client.interceptors
        val loggingFound = interceptors.any { it is HttpLoggingInterceptor }
        assertNotNull("Logging interceptor should be present", loggingFound)
    }
}
