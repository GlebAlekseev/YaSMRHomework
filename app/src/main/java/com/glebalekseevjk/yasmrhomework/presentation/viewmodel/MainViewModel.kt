package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.interactor.GetTokenPairUseCase
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler


class MainViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage,
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage
) : BaseViewModel(application) {
    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, exception ->
            val message = ExceptionHandler.parse(exception)
            _errorHandler.value = message
        }
    private val getTokenPairUseCase = GetTokenPairUseCase(authRepositoryImpl)

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
}
