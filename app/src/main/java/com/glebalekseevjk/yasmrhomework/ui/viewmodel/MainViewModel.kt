package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.interactor.AuthUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.TokenUseCase
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.state.MainState
import javax.inject.Inject
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.interactor.SettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn

class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase,
    private val settingsUseCase: SettingsUseCase
) : BaseViewModel<MainState>(MainState()) {
    init {
        subscribeOnDataSource(authUseCase.isAuth().asLiveData()) { response, state ->
            if (response.status == ResultStatus.SUCCESS) {
                state.copy(
                    login = tokenUseCase.getLogin() ?: "Unknown",
                    displayName = tokenUseCase.getDisplayName() ?: "Unknown",
                    isAuth = response.data
                )
            } else state
        }
        subscribeOnDataSource(settingsUseCase.isDarkTheme().asLiveData()) { response, state ->
            if (response.status == ResultStatus.SUCCESS) {
                state.copy(
                    isDarkMode = response.data
                )
            } else state
        }
    }

    fun getDarkMode(): Boolean = settingsUseCase.getIsDarkTheme()

    fun updateTokenPair(code: String, block: (Result<Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.getTokenPair(code).collect {
                block.invoke(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout().collect {}
        }
    }

    fun setDarkTheme(value: Boolean) {
        viewModelScope.launchWithExceptionHandler {
            settingsUseCase.setDarkTheme(!value).collect()
        }
    }
}