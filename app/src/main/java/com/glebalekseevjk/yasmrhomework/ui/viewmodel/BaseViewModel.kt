package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState.Companion.OK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    protected val _errorHandler = MutableStateFlow(OK)
    val errorHandler: StateFlow<String>
        get() = _errorHandler
    abstract val coroutineExceptionHandler: CoroutineExceptionHandler
    protected fun CoroutineScope.launchWithExceptionHandler(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(coroutineExceptionHandler) {
            block()
        }
    }

    protected fun <T> runBlockingWithExceptionHandler(block: suspend CoroutineScope.() -> T): T {
        return runBlocking(coroutineExceptionHandler) {
            block()
        }
    }
}