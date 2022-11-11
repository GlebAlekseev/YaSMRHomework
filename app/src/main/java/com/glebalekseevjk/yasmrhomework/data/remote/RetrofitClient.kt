package com.glebalekseevjk.yasmrhomework.data.remote

import android.util.Log
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronize.SynchronizedStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://192.168.0.102/"
    private var retrofit: Retrofit.Builder? = null
    private var basicTodoApiClient: OkHttpClient? = null
    private var advancedTodoApiClient: OkHttpClient? = null

    private lateinit var todoApiWithBasicClient: TodoService

    lateinit var todoApi: TodoService
    lateinit var authApi: AuthService

    fun init(
        tokenStorage: TokenStorage,
        revisionStorage: RevisionStorage,
        synchronizedStorage: SynchronizedStorage,
        todoItemDao: TodoItemDao
    ) {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        authApi = retrofit!!
            .build()
            .create(AuthService::class.java)


        basicTodoApiClient = OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor {
                    Log.d("Network", it)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(AuthorizationInterceptor(tokenStorage))
            .addNetworkInterceptor(RevisionInterceptor(revisionStorage))
            .addNetworkInterceptor(
                AuthorizationFailedInterceptor(
                    tokenStorage,
                    revisionStorage,
                    authApi
                )
            )
            .build()
        todoApiWithBasicClient = retrofit!!
            .client(basicTodoApiClient!!)
            .build()
            .create(TodoService::class.java)

        advancedTodoApiClient = OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor {
                    Log.d("Network", it)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(
                SynchronizedInterceptor(
                    synchronizedStorage,
                    revisionStorage,
                    todoApiWithBasicClient,
                    todoItemDao
                )
            )
            .addNetworkInterceptor(AuthorizationInterceptor(tokenStorage))
            .addNetworkInterceptor(RevisionInterceptor(revisionStorage))
            .addNetworkInterceptor(RevisionFailedInterceptor(revisionStorage, todoApiWithBasicClient, todoItemDao))
            .addNetworkInterceptor(
                AuthorizationFailedInterceptor(
                    tokenStorage,
                    revisionStorage,
                    authApi
                )
            )
            .build()

        todoApi = retrofit!!
            .client(advancedTodoApiClient!!)
            .build()
            .create(TodoService::class.java)
    }
}