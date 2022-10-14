package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.data.remote.AuthService
import com.glebalekseevjk.yasmrhomework.data.remote.TodoService
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.features.oauth.TokenStorage
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.features.synchronized.SynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.mapper.Mapper
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.awaitResponse

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
): AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<Boolean>> = flow {
        emit(Result(ResultStatus.LOADING,false))
        val authResponse = authService.getTokenPair(code).awaitResponse()
        val tokenPair = authResponse.body()?.data
        if (authResponse.code() == 200 && tokenPair != null){
            tokenStorage.setTokenPair(tokenPair)
            emit(Result(ResultStatus.SUCCESS,true))
        }else{
            emit(Result(ResultStatus.FAILURE,false))
        }
    }.flowOn(Dispatchers.IO)

    override fun logout(accessToken: String): Flow<Result<Boolean>> = flow {
        emit(Result(ResultStatus.LOADING, false))
        val authResponse = authService.logout(accessToken).awaitResponse()
        if (authResponse.code() == 200){
            emit(Result(ResultStatus.SUCCESS,true))
        }else{
            emit(Result(ResultStatus.FAILURE,false))
        }
    }.flowOn(Dispatchers.IO)
}