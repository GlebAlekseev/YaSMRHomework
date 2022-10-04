package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow
import com.glebalekseevjk.yasmrhomework.domain.entity.Result

class DeleteTodoItemUseCase(
    private val todoListRepository: TodoListRepository,
) {
    operator fun invoke(todoId: Long): Flow<Result<TodoItem>> = todoListRepository.deleteTodoItem(todoId)
}