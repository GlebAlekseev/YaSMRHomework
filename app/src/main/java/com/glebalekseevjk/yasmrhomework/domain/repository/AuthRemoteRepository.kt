package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import kotlinx.coroutines.flow.Flow

interface AuthRemoteRepository {
    fun loginWithYandex(): Flow<TokenPair>
    fun refreshRefreshToken(): Flow<TokenPair>
}