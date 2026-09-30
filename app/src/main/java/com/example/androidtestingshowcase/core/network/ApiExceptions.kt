package com.example.androidtestingshowcase.core.network

sealed class ApiException : RuntimeException() {
    data class ServerError(val code: Int, override val message: String) : ApiException()
    data class ClientError(val code: Int, override val message: String) : ApiException()
    data class NetworkError(override val cause: Throwable) : ApiException()
    data class UnknownError(override val message: String, override val cause: Throwable?) : ApiException()
}
