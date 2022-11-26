package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListLocalRepository
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRemoteRepository
import kotlinx.coroutines.flow.Flow

class TodoItemUseCase (
    private val todoListLocalRepository: TodoListLocalRepository,
    private val todoListRemoteRepository: TodoListRemoteRepository
) {
    fun getTodoItemLocal(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.getTodoItem(todoId)

    fun addTodoItemLocal(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.addTodoItem(todoItem)

    fun deleteTodoItemLocal(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.deleteTodoItem(todoId)

    fun editTodoItemLocal(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.editTodoItem(todoItem)

    fun getTodoListLocal(): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.getTodoList()

    fun replaceTodoListLocal(todoList: List<TodoItem>): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.replaceTodoList(todoList)

    fun deleteTodoListLocal(): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListLocalRepository.deleteTodoList()

    fun getTodoItemRemote(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.getTodoItem(todoId)

    fun addTodoItemRemote(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.addTodoItem(todoItem)

    fun deleteTodoItemRemote(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.deleteTodoItem(todoId)

    fun editTodoItemRemote(todoItem: TodoItem): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.editTodoItem(todoItem)

    fun getTodoListRemote(): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.getTodoList()

    fun replaceTodoListRemote(todoList: List<TodoItem>): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.replaceTodoList(todoList)

    fun deleteTodoListRemote(): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRemoteRepository.deleteTodoList()
}