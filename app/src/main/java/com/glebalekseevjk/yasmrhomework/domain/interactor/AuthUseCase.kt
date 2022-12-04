package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun logout():  Flow<Result<Unit>> =
        authRepository.logout()

    fun getTokenPair(code: String): Flow<Result<Unit>> =
        authRepository.getTokenPair(code)
}