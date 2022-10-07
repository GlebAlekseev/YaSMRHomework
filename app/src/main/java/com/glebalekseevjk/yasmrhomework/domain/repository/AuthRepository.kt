package com.glebalekseevjk.yasmrhomework.domain.repository

import kotlinx.coroutines.flow.Flow
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair

interface AuthRepository {
    fun getTokenPair(code: String): Flow<Result<TokenPair>>
    fun logout(accessToken: String): Flow<Result<Boolean>>
}