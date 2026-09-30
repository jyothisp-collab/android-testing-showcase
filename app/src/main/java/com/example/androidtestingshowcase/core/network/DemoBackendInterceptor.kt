package com.example.androidtestingshowcase.core.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoBackendInterceptor @Inject constructor() : Interceptor {

    private val json = "application/json".toMediaType()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        val response = when {
            path == "/items" -> ok(
                """
                [
                  {
                    "id": "testing-patterns",
                    "title": "Testing Patterns",
                    "detail": "Unit tests with Turbine, Fake repos, and MockWebServer.",
                    "updatedAtMillis": 1726000000000
                  },
                  {
                    "id": "room-caching",
                    "title": "Room Caching",
                    "detail": "In-memory Room databases for offline-first testing.",
                    "updatedAtMillis": 1727000000000
                  }
                ]
                """.trimIndent()
            )
            path.startsWith("/items/") -> {
                val id = path.substringAfterLast("/")
                when (id) {
                    "testing-patterns" -> ok(
                        """
                        {
                          "id": "testing-patterns",
                          "title": "Testing Patterns",
                          "detail": "Unit tests with Turbine, Fake repos, and MockWebServer.",
                          "updatedAtMillis": 1726000000000
                        }
                        """.trimIndent()
                    )
                    "room-caching" -> ok(
                        """
                        {
                          "id": "room-caching",
                          "title": "Room Caching",
                          "detail": "In-memory Room databases for offline-first testing.",
                          "updatedAtMillis": 1727000000000
                        }
                        """.trimIndent()
                    )
                    else -> notFound()
                }
            }
            else -> notFound()
        }

        return response
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .build()
    }

    private fun ok(body: String) = baseResponse(200, "OK", body)

    private fun notFound() = baseResponse(404, "Not Found", """{"message":"not found"}""")

    private fun baseResponse(code: Int, message: String, body: String) = Response.Builder()
        .code(code)
        .message(message)
        .body(body.toResponseBody(json))

    companion object {
        const val SIMULATED_DELAY_MS = 50L
    }
}
