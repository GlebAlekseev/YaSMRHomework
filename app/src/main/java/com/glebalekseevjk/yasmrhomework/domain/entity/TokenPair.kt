package com.glebalekseevjk.yasmrhomework.domain.entity

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val login: String,
    val displayName: String
    )