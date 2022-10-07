package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoListFragment
import com.glebalekseevjk.yasmrhomework.utils.ExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainViewModel(
    application: Application,
    authRepositoryImpl: AuthRepositoryImpl,
    private val sharedPreferencesTokenStorage: SharedPreferencesTokenStorage,
    private val sharedPreferencesRevisionStorage: SharedPreferencesRevisionStorage
    ): AndroidViewModel(application) {
    private val logoutUseCase = LogoutUseCase(authRepositoryImpl)
    private val getTokenPairUseCase = GetTokenPairUseCase(authRepositoryImpl)




    fun checkAuth(block: ()->Unit){
        val tokenPair = sharedPreferencesTokenStorage.getTokenPair()
        // Если refresh токена нет или он просрочен
        if(tokenPair == null){
            sharedPreferencesTokenStorage.clear()
            sharedPreferencesRevisionStorage.clear()
            // Удалить КЕШ записи
            // Пройти авторизацию снова
        }else{
            // Аккаунт аутентифицирован
            block()
        }
    }

    fun getAndSetTokens(code: String, block: (Result<TokenPair>, SharedPreferencesTokenStorage)->Unit){
        viewModelScope.launch {
            getTokenPairUseCase(code).collect{
                block(it, sharedPreferencesTokenStorage)
            }
        }
    }
}
