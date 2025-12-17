package org.cescfe.book_publishing_app.data.shared.repository.helper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

object TestHttpExceptionFactory {
    fun create(code: Int): HttpException {
        val responseBody = "Error".toResponseBody("application/json".toMediaType())
        val response = Response.error<Any>(code, responseBody)
        return HttpException(response)
    }

    fun create(code: Int, message: String): HttpException {
        val responseBody = message.toResponseBody("application/json".toMediaType())
        val response = Response.error<Any>(code, responseBody)
        return HttpException(response)
    }

    fun create(code: Int, errorBody: ResponseBody): HttpException {
        val response = Response.error<Any>(code, errorBody)
        return HttpException(response)
    }
}
