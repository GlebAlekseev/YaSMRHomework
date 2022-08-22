package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository

class GetTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(id: String): TodoItem? = todoItemsRepository.getTodoItem(id)
}