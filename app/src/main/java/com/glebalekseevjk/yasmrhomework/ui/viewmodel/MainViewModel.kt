package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.interactor.AuthUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.RevisionUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.TokenUseCase
import com.glebalekseevjk.yasmrhomework.domain.repository.AuthRepository
import com.glebalekseevjk.yasmrhomework.domain.repository.RevisionRepository
import com.glebalekseevjk.yasmrhomework.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineExceptionHandler


class MainViewModel(
    authRepository: AuthRepository,
    tokenRepository: TokenRepository,
    revisionRepository: RevisionRepository
    ) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }
    private val authUseCase = AuthUseCase(authRepository)
    private val tokenUseCase = TokenUseCase(tokenRepository)
    private val revisionUseCase = RevisionUseCase(revisionRepository)

    val isAuth: Boolean
        get() {
            val tokenPair = tokenUseCase.getTokenPair()
            return if (tokenPair == null) {
                revisionUseCase.clear()
                // Удалить кеш записи в браузере
                false
            } else true
        }

    fun updateTokenPair(code: String, block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.getTokenPair(code).collect {
                block(it)
            }
        }
    }

    fun logout(block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout().collect {
                block(it)
            }
        }
    }
}