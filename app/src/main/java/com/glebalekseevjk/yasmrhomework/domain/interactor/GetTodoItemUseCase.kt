package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.*
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow



class GetTodoItemUseCase(
    private val todoListRepository: TodoListRepository,
) {
    operator fun invoke(todoId: Long): Flow<Result<TodoItem>> = todoListRepository.getTodoItem(todoId)
}