package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow

class GetTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(id: String): Flow<Result<TodoItem>> = todoItemsRepository.getTodoItem(id)
}