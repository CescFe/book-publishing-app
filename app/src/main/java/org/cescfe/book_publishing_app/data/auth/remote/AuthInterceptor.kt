package org.cescfe.book_publishing_app.data.auth.remote

import okhttp3.Interceptor
import okhttp3.Response
import org.cescfe.book_publishing_app.data.auth.TokenManager

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = TokenManager.getToken()

        if (token == null || originalRequest.url.encodedPath.contains("auth/login")) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
