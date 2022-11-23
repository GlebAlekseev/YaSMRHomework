package com.glebalekseevjk.yasmrhomework.domain.feature

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair

interface TokenStorage {
    fun getTokenPair(): TokenPair?
    fun setTokenPair(tokenPair: TokenPair)
    fun clear()
    fun getExpiresAt(): Long?
    fun getRefreshToken(): String?
    fun getAccessToken(): String?
}