package com.jenzz.demo.common

sealed class ApiResponse<out T : Any> {

    data class Success<T : Any>(val data: T) : ApiResponse<T>()
    data class Error(val error: Throwable) : ApiResponse<Nothing>()
}
