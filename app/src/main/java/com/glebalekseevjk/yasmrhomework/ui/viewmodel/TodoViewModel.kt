package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import com.glebalekseevjk.yasmrhomework.ui.fragment.TodoFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class TodoViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val tokenUseCase: TokenUseCase,
    private val revisionUseCase: RevisionUseCase,
    private val todoItemUseCase: TodoItemUseCase,
    private val schedulerUseCase: SchedulerUseCase
) : BaseViewModel() {

    override val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            val message = exception.message ?: "Неизвестная ошибка"
            _errorHandler.value = message
        }

    fun setCurrentTodoItemById(todoId: Long) {
        runBlockingWithExceptionHandler {
            val getResult =
                todoItemUseCase.getTodoItemLocal(todoId).first { it.status != ResultStatus.LOADING }
            if (getResult.status == ResultStatus.SUCCESS) {
                currentTodoItem.value = getResult.data.first[0]
            } else {
                throw RuntimeException("БД не может получить элемент с id: $todoId")
            }
        }
    }

    fun addTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val addResult =
                todoItemUseCase.addTodoItemLocal(todoItem).first { it.status != ResultStatus.LOADING }
            if (addResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.addTodoItemRemote(todoItem).collect { result ->
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    }else{
                        block.invoke(Result(result.status, TodoItem.PLUG, result.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может добавить todoItem: $todoItem")
            }
        }
    }

    fun editTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val editResult =
                todoItemUseCase.editTodoItemLocal(todoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.editTodoItemRemote(todoItem).collect { result ->
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    }else{
                        block.invoke(Result(result.status, TodoItem.PLUG, result.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может отредактировать todoItem: $todoItem")
            }
        }
    }


    fun deleteTodo(
        todoItem: TodoItem,
        snackBarBlock: (todoItem: TodoItem) -> Boolean,
        block: (Result<TodoItem>) -> Unit
    ) {
        viewModelScope.launchWithExceptionHandler {
            val deleteResult =
                todoItemUseCase.deleteTodoItemLocal(todoItem.id).first { it.status != ResultStatus.LOADING }
            val deletedItem = if (deleteResult.status == ResultStatus.SUCCESS) {
                deleteResult.data.first[0]
            } else {
                throw RuntimeException("БД не может удалить запись todoItem: $todoItem")
            }
            if (snackBarBlock.invoke(deletedItem)) {
                val addResult =
                    todoItemUseCase.addTodoItemLocal(deletedItem).first { it.status != ResultStatus.LOADING }
                if (addResult.status != ResultStatus.SUCCESS) throw RuntimeException("БД не может добавить todoItem: $deletedItem")
            } else {
                todoItemUseCase.deleteTodoItemRemote(deletedItem.id).collect { result ->
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    }else{
                        block.invoke(Result(result.status, TodoItem.PLUG, result.message))
                    }
                }
            }
        }
    }

    fun setupOneTimeCheckSynchronize(){
        schedulerUseCase.setupOneTimeCheckSynchronize()
    }

    val isAuth: Boolean
        get() {
            val tokenPair = tokenUseCase.getTokenPair()
            return if (tokenPair == null) {
                logout()
                false
            } else true
        }

    private fun logout() {
        viewModelScope.launchWithExceptionHandler {
            authUseCase.logout()
        }
    }

    fun onMessageChanged(text: CharSequence){
        currentTodoItem.value =
            currentTodoItem.value.copy(text = text.toString())
    }

    var currentTodoItem: MutableStateFlow<TodoItem> =  MutableStateFlow(TodoItem.PLUG)

    private var _currentScreenMode: MutableStateFlow<String> =  MutableStateFlow(MODE_ADD)
    var currentScreenMode: StateFlow<String> =  _currentScreenMode
    fun setCurrentScreenMode(screenMode: String){
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD)
            throw RuntimeException("Unknown screen mode $screenMode")
        _currentScreenMode.tryEmit(screenMode)
    }

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
    }
}
