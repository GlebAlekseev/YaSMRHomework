package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.data.local.dao.TodoItemDao
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.remote.AuthService
import com.glebalekseevjk.yasmrhomework.data.remote.model.AuthResponse
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class AuthRepositoryImpl(
    private val todoItemDao: TodoItemDao,
    private val authService: AuthService,
    private val tokenStorage: SharedPreferencesTokenStorage,
    private val revisionStorage: SharedPreferencesRevisionStorage,
    private val synchronizedStorage: SharedPreferencesSynchronizedStorage,
) : AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        val authResponse = runCatching {
            authService.getTokenPair(code).execute()
        }.getOrNull()
        val result = getResultFromAuthResponse(authResponse)
        if (result.status == ResultStatus.SUCCESS){
            tokenStorage.setTokenPair(authResponse!!.body()!!.data!!)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun logout(): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        var message = ""
        try {
            tokenStorage.clear()
            revisionStorage.clear()
            synchronizedStorage.setSynchronizedStatus(SharedPreferencesSynchronizedStorage.SYNCHRONIZED)
            todoItemDao.deleteAll()
        } catch (err: Exception) {
            message = "Ошибка выхода"
        }
        emit(Result(ResultStatus.SUCCESS, Unit, message))
    }.flowOn(Dispatchers.IO)

    private fun getResultFromAuthResponse(authResponse: Response<AuthResponse>?): Result<Unit> {
        var message = ""
        var status = ResultStatus.SUCCESS
        val data = Unit
        authResponse?.body()?.data ?: run {
            message = "Ошибка парсинга ответа"
        }
        authResponse ?: run { message = "Нет соединения" }
        authResponse?.code().let {
            when (it) {
                200 -> {}
                400 -> {
                    message = "Ошибка клиента"
                }
                401 -> {
                    message = "Не авторизован"
                    status = ResultStatus.UNAUTHORIZED
                }
                500 -> {
                    message = "Ошибка сервера"
                }
                else -> {
                    message = "Неизвестная ошибка"
                }
            }
        }
        if (message != "") {
            status = ResultStatus.FAILURE
        }
        return Result(status, data, message)
    }
}