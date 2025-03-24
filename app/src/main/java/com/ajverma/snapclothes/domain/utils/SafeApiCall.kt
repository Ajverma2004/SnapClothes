package com.ajverma.snapclothes.domain.utils

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: HttpException) {
        Resource.Error(
            message = when (e.code()) {
                400 -> "Bad Request"
                401 -> "Unauthorized"
                500 -> "Server error"
                else -> "Unexpected error"
            },
            code = e.code(),
            throwable = e
        )
    } catch (e: IOException) {
        Resource.Error(
            message = "Network error. Check your internet connection.",
            throwable = e
        )
    } catch (e: Exception) {
        Resource.Error(
            message = "Something went wrong.",
            throwable = e
        )
    }
}
