package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*

abstract class BaseViewModel<T>(initState: T) : ViewModel() {
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            // Логгирование для разработчика, без падения приложения
            // Уведомление об ошибке notify
        }

    protected fun CoroutineScope.launchWithExceptionHandler(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch(coroutineExceptionHandler) {
            block.invoke(this)
        }
    }

    protected fun <T> runBlockingWithExceptionHandler(block: suspend CoroutineScope.() -> T): T {
        return runBlocking(coroutineExceptionHandler) {
            block.invoke(this)
        }
    }

    // Шина состояний
    private val _state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }
    val state: LiveData<T> = _state

    val currentState
        get() = _state.value!!

    fun updateState(update: (currentState: T) -> T) {
        val updatedState: T = update(currentState)
        _state.value = updatedState!!
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        _state.observe(owner, Observer { onChanged(it!!) })
    }

    protected fun <S> subscribeOnDataSource(
        source: LiveData<S>,
        onChanged: (newValue: S, currentState: T) -> T?
    ) {
        _state.addSource(source) {
            _state.value = onChanged(it, currentState) ?: return@addSource
        }
    }

    // Шина уведомлений
    private val notifications = MutableLiveData<Event<Notify>>()

    fun notify(content: Notify) {
        notifications.value = Event(content)
    }

    fun observeNotifications(owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit) {
        notifications.observe(owner, EventObserver { onNotify(it) })
    }

}

// Не выводить уже показанное уведомление повторно
class Event<out E>(private val content: E) {
    var hasBeenHandled = false

    fun getContentIfNotHandled(): E? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): E = content
}

class EventObserver<E>(private val onEventUnhandledContent: (E) -> Unit) : Observer<Event<E>> {

    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

// Типы уведомлений, обрабатываемые на стороне UI
sealed class Notify(val message: String) {
    data class TextMessage(val msg: String) : Notify(msg)

    data class ActionMessage(
        val msg: String,
        val actionLabel: String,
        val actionHandler: (() -> Unit)
    ) : Notify(msg)

    data class ErrorMessage(
        val msg: String,
        val errLabel: String?,
        val errHandler: (() -> Unit)?
    ) : Notify(msg)
}