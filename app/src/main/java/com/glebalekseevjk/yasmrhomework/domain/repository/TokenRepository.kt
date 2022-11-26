package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair

interface TokenRepository {
    fun getTokenPair(): TokenPair?
    fun setTokenPair(tokenPair: TokenPair)
    fun clear()
    fun getExpiresAt(): Long?
    fun getRefreshToken(): String?
    fun getAccessToken(): String?
    fun getLogin(): String?
    fun getDisplayName(): String?
}