package com.glebalekseevjk.yasmrhomework.di.module

import android.content.Context
import android.util.Log
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.remote.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier


@Module
interface RemoteStorageModule {

    companion object {
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor {
                Log.d("Network", it)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @Provides
        fun provideRetrofitBuilder(context: Context): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(context.resources.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
        }

        @Base
        @Provides
        fun provideOkHttpClientBase(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .build()
        }

        @Short
        @Provides
        fun provideOkHttpClientShort(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authorizationFailedInterceptor: AuthorizationFailedInterceptor,
            authorizationInterceptor: AuthorizationInterceptor,
            revisionInterceptor: RevisionInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(authorizationFailedInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .addNetworkInterceptor(revisionInterceptor)
                .build()
        }

        @Full
        @Provides
        fun provideOkHttpClientFull(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            synchronizedInterceptor: SynchronizedInterceptor,
            revisionFailedInterceptor: RevisionFailedInterceptor,
            authorizationFailedInterceptor: AuthorizationFailedInterceptor,
            authorizationInterceptor: AuthorizationInterceptor,
            revisionInterceptor: RevisionInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(synchronizedInterceptor)
                .addInterceptor(revisionFailedInterceptor)
                .addInterceptor(authorizationFailedInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .addNetworkInterceptor(revisionInterceptor)
                .build()
        }

        @Provides
        fun provideAuthService(
            retrofitBuilder: Retrofit.Builder,
            @Base okHttpClient: OkHttpClient
        ): AuthService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(AuthService::class.java)
        }

        @Short
        @Provides
        fun provideTodoServiceShort(
            retrofitBuilder: Retrofit.Builder,
            @Short okHttpClient: OkHttpClient
        ): TodoService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(TodoService::class.java)
        }


        @Provides
        fun provideTodoServiceFull(
            retrofitBuilder: Retrofit.Builder,
            @Full okHttpClient: OkHttpClient
        ): TodoService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(TodoService::class.java)
        }


        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Base

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Short

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Full

    }
}


