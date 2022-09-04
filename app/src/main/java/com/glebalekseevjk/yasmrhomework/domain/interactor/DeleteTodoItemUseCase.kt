package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import com.glebalekseevjk.yasmrhomework.domain.entity.Result

class DeleteTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(todoItem: TodoItem): Flow<Result<TodoItem>> =  todoItemsRepository.deleteTodoItem(todoItem)
    operator fun invoke(todoId: String): Flow<Result<TodoItem>> = todoItemsRepository.deleteTodoItem(todoId)
}