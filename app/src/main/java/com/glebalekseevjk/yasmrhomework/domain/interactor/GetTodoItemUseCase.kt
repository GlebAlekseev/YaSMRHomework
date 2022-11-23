package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow


class GetTodoItemUseCase(
    private val todoListRepository: TodoListRepository,
) {
    operator fun invoke(todoId: Long): Flow<Result<Pair<List<TodoItem>, Revision>>> =
        todoListRepository.getTodoItem(todoId)
}