package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.repository.TokenRepository
import javax.inject.Inject

class TokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    fun getAccessToken(): String? =
        tokenRepository.getAccessToken()

    fun getRefreshToken(): String? =
        tokenRepository.getRefreshToken()

    fun getDisplayName(): String? =
        tokenRepository.getDisplayName()

    fun getLogin(): String? =
        tokenRepository.getLogin()

    fun getExpiresAt(): Long? =
        tokenRepository.getExpiresAt()

    fun clear() = tokenRepository.clear()

    fun getTokenPair(): TokenPair? =
        tokenRepository.getTokenPair()

    fun setTokenPair(tokenPair: TokenPair) =
        tokenRepository.setTokenPair(tokenPair)
}