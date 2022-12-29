package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.interactor.AuthUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.TokenUseCase
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.state.MainState
import javax.inject.Inject
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import kotlinx.coroutines.flow.collect

class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase,
) : BaseViewModel<MainState>(MainState()) {
    init {
        subscribeOnDataSource(authUseCase.isAuth().asLiveData()){ response, state ->
            println("*********************** subscribeOnDataSource isAuth=${response.data}")
            if (response.status == ResultStatus.SUCCESS){
                state.copy(
                    login = tokenUseCase.getLogin() ?: "Unknown",
                    displayName= tokenUseCase.getDisplayName() ?: "Unknown",
                    isAuth = response.data
                )
            } else state
        }
    }

    fun updateTokenPair(code: String, block: (Result <Unit>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.getTokenPair(code).collect {
                block.invoke(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout().collect{}
        }
    }
}