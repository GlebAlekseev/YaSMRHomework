package com.glebalekseevjk.yasmrhomework.ui.viewmodel

import android.view.View
import android.view.View.OnClickListener
import android.widget.Spinner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.DAY_MILLIS
import com.glebalekseevjk.yasmrhomework.domain.interactor.AuthUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.RevisionUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.SchedulerUseCase
import com.glebalekseevjk.yasmrhomework.domain.interactor.TodoItemUseCase
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.state.TodoState
import com.glebalekseevjk.yasmrhomework.utils.CustomOnClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class TodoViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val revisionUseCase: RevisionUseCase,
    private val todoItemUseCase: TodoItemUseCase,
    private val schedulerUseCase: SchedulerUseCase,
) : BaseViewModel<TodoState>(TodoState()) {
    init {
        subscribeOnDataSource(authUseCase.isAuth().asLiveData()) { response, state ->
            if (response.status == ResultStatus.SUCCESS) {
                state.copy(
                    isAuth = response.data
                )
            } else state
        }
    }

    fun setCurrentTodoItemById(todoId: Long) {
        runBlockingWithExceptionHandler {
            val getResult =
                todoItemUseCase.getTodoItemLocal(todoId).first { it.status != ResultStatus.LOADING }
            when (getResult.status) {
                ResultStatus.SUCCESS -> {
                    updateState {
                        it.copy(
                            todoItem = getResult.data.first[0],
                            isLoadingTodoItem = false
                        )
                    }
                }
                ResultStatus.LOADING -> {
                    updateState {
                        it.copy(
                            isLoadingTodoItem = true
                        )
                    }
                }
                else -> {
                    throw RuntimeException("БД не может получить элемент с id: $todoId")
                }
            }
        }
    }

    fun addTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val addResult =
                todoItemUseCase.addTodoItemLocal(todoItem)
                    .first { it.status != ResultStatus.LOADING }
            if (addResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.addTodoItemRemote(todoItem).collect { result ->
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
                throw RuntimeException("БД не может добавить todoItem: $todoItem")
            }
        }
    }

    fun editTodo(todoItem: TodoItem, block: (Result<TodoItem>) -> Unit) {
        viewModelScope.launchWithExceptionHandler {
            val editResult =
                todoItemUseCase.editTodoItemLocal(todoItem)
                    .first { it.status != ResultStatus.LOADING }
            if (editResult.status == ResultStatus.UNAUTHORIZED) authUseCase.logout()
            if (editResult.status == ResultStatus.SUCCESS) {
                todoItemUseCase.editTodoItemRemote(todoItem).collect { result ->
                    if (result.status == ResultStatus.SUCCESS) {
                        val response = result.data
                        revisionUseCase.setRevision(response.second)
                        block.invoke(Result(result.status, result.data.first[0], result.message))
                    } else {
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
        snackBarBlock: suspend (todoItem: TodoItem) -> Boolean,
        block: (Result<TodoItem>) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launchWithExceptionHandler {
            val deleteResult =
                todoItemUseCase.deleteTodoItemLocal(todoItem.id)
                    .first { it.status != ResultStatus.LOADING }
            if (deleteResult.status == ResultStatus.UNAUTHORIZED) authUseCase.logout()
            val deletedItem = if (deleteResult.status == ResultStatus.SUCCESS) {
                deleteResult.data.first[0]
            } else {
                throw RuntimeException("БД не может удалить запись todoItem: $todoItem")
            }
            if (snackBarBlock.invoke(deletedItem)) {
                val addResult =
                    todoItemUseCase.addTodoItemLocal(deletedItem)
                        .first { it.status != ResultStatus.LOADING }
                if (addResult.status != ResultStatus.SUCCESS) throw RuntimeException("БД не может добавить todoItem: $deletedItem")
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

    fun setupOneTimeCheckSynchronize() {
        schedulerUseCase.setupOneTimeCheckSynchronize()
    }

    fun onMessageChanged(text: CharSequence) {
        updateState {
            it.copy(
                todoItem = it.todoItem.copy(text = text.toString())
            )
        }
    }

    fun setCurrentScreenMode(screenMode: String) {
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD)
            throw RuntimeException("Unknown screen mode $screenMode")
        updateState {
            it.copy(
                screenMode = screenMode
            )
        }
    }

    fun setDeadline(checked: Boolean, openDatePicker: CustomOnClickListener) {
        if (checked) {
            updateState {
                it.copy(
                    todoItem = it.todoItem.copy(
                        deadline = System.currentTimeMillis() + DAY_MILLIS,
                    )
                )
            }
            openDatePicker.invoke()
        } else {
            updateState {
                it.copy(
                    todoItem = it.todoItem.copy(
                        deadline = null
                    )
                )
            }
        }
    }

//    fun setImportant(view: Spinner, value: String){
    fun setImportant(pos: Int){
        val important = when(pos){
            0 -> Importance.LOW
            1 -> Importance.BASIC
            2 -> Importance.IMPORTANT
            else -> throw RuntimeException("Unknown Importance type")
        }
        updateState {
            it.copy(
                todoItem = it.todoItem.copy(
                    importance = important
                )
            )
        }
    }

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
    }
}
