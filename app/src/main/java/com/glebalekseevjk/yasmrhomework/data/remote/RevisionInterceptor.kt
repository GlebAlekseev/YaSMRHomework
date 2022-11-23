package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.domain.feature.RevisionStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RevisionInterceptor(private val revisionStorage: RevisionStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addRevisionHeader()
            .let { chain.proceed(it) }
    }

    private fun Request.addRevisionHeader(): Request {
        val authHeaderName = "X-Last-Known-Revision"
        return newBuilder()
            .apply {
                val revision = revisionStorage.getRevision()
                if (revision != null) {
                    header(authHeaderName, revision.revision.toString())
                }
            }
            .build()
    }
}