package com.glebalekseevjk.yasmrhomework.data.remote

import com.glebalekseevjk.yasmrhomework.data.remote.model.AuthResponse
import com.glebalekseevjk.yasmrhomework.data.remote.model.RefreshToken
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @GET("auth/token")
    fun getTokenPair(@Query("code") code: String): Call<AuthResponse>

    @POST("auth/refresh")
    fun refreshTokenPair(@Body refreshToken: RefreshToken): Call<AuthResponse>
}