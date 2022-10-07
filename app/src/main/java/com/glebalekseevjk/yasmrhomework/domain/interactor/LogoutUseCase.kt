package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.*
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow



class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(accessToken: String): Flow<Result<Boolean>> = authRepository.logout(accessToken)
}