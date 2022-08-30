package com.glebalekseevjk.yasmrhomework.domain.interactor

import androidx.lifecycle.LiveData
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository

class GetTodoListUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(callback: (List<TodoItem>)->Unit): List<TodoItem> = todoItemsRepository.getTodoList(callback)
}