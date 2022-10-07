package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl: AuthRepository {
    override fun getTokenPair(code: String): Flow<Result<TokenPair>> {
        return flow {  }
    }

    override fun logout(accessToken: String): Flow<Result<Boolean>> {
        return flow {  }
    }
}