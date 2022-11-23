package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.interactor.GetTokenPairUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.LogoutUseCase
import kotlinx.coroutines.CoroutineExceptionHandler


class MainViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
) : BaseViewModel(application) {
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage =
        SharedPreferencesTokenStorage(application)
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage =
        SharedPreferencesRevisionStorage(application)


    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }
    private val getTokenPairUseCase = GetTokenPairUseCase(authRepositoryImpl)
    private val logoutUseCase = LogoutUseCase(authRepositoryImpl)

    val isAuth: Boolean
        get() {
            val tokenPair = sharedPreferencesTokenStorage.getTokenPair()
            return if (tokenPair == null) {
                sharedPreferencesRevisionStorage.clear()
                // Удалить кеш записи в браузере
                false
            } else true
        }

    fun updateTokenPair(code: String, block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            getTokenPairUseCase(code).collect {
                block(it)
            }
        }
    }

    fun logout(block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            logoutUseCase().collect {
                block(it)
            }
        }
    }
}