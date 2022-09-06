package com.glebalekseevjk.yasmrhomework.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    protected val _errorHandler = MutableStateFlow(-1)
    val errorHandler: StateFlow<Int>
        get() = _errorHandler
    abstract val coroutineExceptionHandler: CoroutineExceptionHandler
    protected fun CoroutineScope.launchCoroutine(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(coroutineExceptionHandler) {
            block()
        }
    }
}
