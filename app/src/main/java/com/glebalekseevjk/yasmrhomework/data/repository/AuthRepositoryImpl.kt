package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.remote.AuthService
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.awaitResponse

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        val authResponse = authService.getTokenPair(code).awaitResponse()
        val tokenPair = authResponse.body()?.data
        if (authResponse.code() == 200 && tokenPair != null) {
            tokenStorage.setTokenPair(tokenPair)
            emit(Result(ResultStatus.SUCCESS, Unit))
        } else {
            emit(Result(ResultStatus.FAILURE, Unit))
        }
    }.flowOn(Dispatchers.IO)

    override fun logout(accessToken: String): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        val authResponse = authService.logout(accessToken).awaitResponse()
        if (authResponse.code() == 200) {
            emit(Result(ResultStatus.SUCCESS, Unit))
        } else {
            emit(Result(ResultStatus.FAILURE, Unit))
        }
    }.flowOn(Dispatchers.IO)
}