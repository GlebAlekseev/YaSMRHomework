package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListLocalRepositoryImpl
import com.glebalekseevjk.yasmrhomework.data.repository.TodoListRemoteRepositoryImpl
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactories.getValue(modelClass as Class<ViewModel>).get() as T
    }
}