package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getTokenPair(code: String): Flow<Result<Unit>>
    fun logout(): Flow<Result<Unit>>
}