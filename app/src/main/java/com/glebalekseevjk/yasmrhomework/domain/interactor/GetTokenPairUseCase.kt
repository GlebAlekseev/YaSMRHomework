package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.*
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow



class GetTokenPairUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(code: String): Flow<Result<Boolean>> = authRepository.getTokenPair(code)
}