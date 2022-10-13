package com.glebalekseevjk.yasmrhomework.data.remote

import android.util.Log
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://192.168.0.102/"
    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit.Builder? = null

    fun init(tokenStorage: TokenStorage, revisionStorage: RevisionStorage){
        okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor {
                    Log.d("Network",it)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(AuthorizationInterceptor(tokenStorage))
            .addNetworkInterceptor(RevisionInterceptor(revisionStorage))
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val todoApi: TodoService by lazy {
        retrofit!!
            .client(okHttpClient!!)
            .build()
            .create(TodoService::class.java)
    }
    val authApi: AuthService by lazy {
        retrofit!!
            .build()
            .create(AuthService::class.java)
    }
}