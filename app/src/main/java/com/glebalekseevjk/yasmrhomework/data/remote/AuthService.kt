package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.data.remote.model.RefreshToken
import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoItemResponse
import com.glebalekseevjk.yasmrhomework.data.remote.model.TodoListResponse
import com.glebalekseevjk.yasmrhomework.data.remote.model.TokenPairResponse
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @GET("auth/token")
    suspend fun getTokenPair(@Query("code") code: String): Call<TokenPairResponse>

    @POST("auth/refresh")
    suspend fun refreshTokenPair(@Body refreshToken: RefreshToken): Call<TokenPairResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") accessToken: String): Call<okhttp3.Response>
}