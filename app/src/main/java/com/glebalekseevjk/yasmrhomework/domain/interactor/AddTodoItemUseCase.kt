package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow

class AddTodoItemUseCase(
    private val todoListRepository: TodoListRepository,
) {
    operator fun invoke(todoItem: TodoItem): Flow<Result<TodoItem>> = todoListRepository.addTodoItem(todoItem)
}