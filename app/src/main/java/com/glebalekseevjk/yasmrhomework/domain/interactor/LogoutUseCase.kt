package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<Result<Unit>> =
        authRepository.logout()
}