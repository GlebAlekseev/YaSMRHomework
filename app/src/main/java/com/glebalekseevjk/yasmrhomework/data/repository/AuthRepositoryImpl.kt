package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.remote.AuthService
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.data.remote.model.AuthResponse
import com.glebalekseevjk.yasmrhomework.data.remote.model.RefreshToken
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        val authResult = runCatching {
            authService.getTokenPair(code).execute()
        }.getOrNull()
        if (authResult != null && authResult.code() == 200 && authResult.body()?.data != null) {
            val tokenPair = authResult.body()!!.data!!
            tokenStorage.setTokenPair(tokenPair)
            emit(Result(ResultStatus.SUCCESS, Unit))
        } else {
            emit(Result(ResultStatus.FAILURE, Unit))
        }
    }.flowOn(Dispatchers.IO)

    override fun logout(): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        // Удаление всех данных
        tokenStorage.clear()
        emit(Result(ResultStatus.SUCCESS, Unit))
    }.flowOn(Dispatchers.IO)
}