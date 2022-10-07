package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val tokenStorage: TokenStorage): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addTokenHeader()
            .let { chain.proceed(it) }
    }
    private fun Request.addTokenHeader(): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .apply {
                val token = tokenStorage.getAccessToken()
                if (token != null) {
                    header(authHeaderName, token.withBearer())
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
}