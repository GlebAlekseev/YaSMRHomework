package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.interactor.AuthUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.RevisionUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.TokenUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase,
    private val revisionUseCase: RevisionUseCase
) : BaseViewModel() {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

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
                block.invoke(it)
            }
        }
    }

    fun logout(block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout().collect {
                block.invoke(it)
            }
        }
    }

    fun getLogin(): String {
        return tokenUseCase.getLogin() ?: "Unknown"
    }

    fun getDisplayName(): String {
        return tokenUseCase.getDisplayName() ?: "Unknown"
    }
}