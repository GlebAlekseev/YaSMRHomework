package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.interactor.*
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.state.TodoListState
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val revisionUseCase: RevisionUseCase,
    private val todoItemUseCase: TodoItemUseCase,
    private val schedulerUseCase: SchedulerUseCase,
) : BaseViewModel<TodoListState>(TodoListState()) {
    init {
        subscribeOnDataSource(authUseCase.isAuth().asLiveData()){ response, state ->
            if (response.status == ResultStatus.SUCCESS){
                state.copy(
                    isAuth = response.data
                )
            } else state
        }
        subscribeOnDataSource(todoItemUseCase.getTodoListLocal().asLiveData()){ response, state ->
            when (response.status) {
                ResultStatus.SUCCESS -> {
                    state.copy(
                        listTodoItem = response.data.first,
                        isLoadingListTodoItem = false
                    )
                }
                ResultStatus.LOADING -> {
                    state.copy(
                        isLoadingListTodoItem = true
                    )
                }
                else -> state
            }
        }
    }

    fun synchronizeTodoList(block: (Result<List<TodoItem>>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            todoItemUseCase.getTodoListRemote().collect { result ->
                if (result.status == ResultStatus.UNAUTHORIZED) authUseCase.logout()
                if (result.status == ResultStatus.SUCCESS) {
                    val response = result.data
                    val replaceResult =
                        todoItemUseCase.replaceTodoListLocal(response.first).first { it.status != ResultStatus.LOADING }
                    if (replaceResult.status == ResultStatus.SUCCESS) {
                        revisionUseCase.setRevision(response.second)
                    } else {
                        throw RuntimeException("БД не может заменить все записи")
                    }
                }
                block.invoke(Result(result.status, result.data.first, result.message))
            }
        }
    }

    fun deleteTodo(
        todoItem: TodoItem,
        snackBarBlock: suspend (todoItem: TodoItem) -> Boolean,
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
            if (snackBarBlock(deletedItem)) {
                val addResult =
                    todoItemUseCase.addTodoItemLocal(deletedItem).first { it.status != ResultStatus.LOADING }
                if (addResult.status != ResultStatus.SUCCESS)
                    throw RuntimeException("БД не может добавить todoItem: $deletedItem")
            } else {
                todoItemUseCase.deleteTodoItemRemote(deletedItem.id).collect { result ->
                    if (result.status == ResultStatus.UNAUTHORIZED) authUseCase.logout()
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    } else {
                        block.invoke(Result(result.status, TodoItem.PLUG, result.message))
                    }
                }
            }
        }
    }

    fun finishTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        val newTodoItem = todoItem.copy(done = true)
        viewModelScope.launchWithExceptionHandler {
            val editResult =
                todoItemUseCase.editTodoItemLocal(newTodoItem).first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.editTodoItemRemote(newTodoItem).collect { result ->
                    if (result.status == ResultStatus.UNAUTHORIZED) authUseCase.logout()
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    } else {
                        block.invoke(Result(result.status, TodoItem.PLUG, result.message))
                    }
                }
            } else {
                throw RuntimeException("БД не может отредактировать todoItem: $newTodoItem")
            }
        }
    }

    fun setupOneTimeCheckSynchronize(){
        schedulerUseCase.setupOneTimeCheckSynchronize()
    }

    fun toggleViewFinished(){
        updateState {
            it.copy(
                isShowFinished = !it.isShowFinished
            )
        }
    }
}
