package com.glebalekseevjk.yasmrhomework.data.remote.model

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair

data class AuthResponse (
    val status: Int,
    val message: String,
    val data: TokenPair?
    )