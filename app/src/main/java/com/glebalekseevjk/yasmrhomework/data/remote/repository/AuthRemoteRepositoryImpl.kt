package com.glebalekseevjk.yasmrhomework.data.remote.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRemoteRepository
import kotlinx.coroutines.flow.Flow

class AuthRemoteRepositoryImpl: AuthRemoteRepository {
    override fun loginWithYandex(): Flow<TokenPair> {
        TODO("Not yet implemented")
    }

    override fun refreshRefreshToken(): Flow<TokenPair> {
        TODO("Not yet implemented")
    }
}