package com.glebalekseevjk.yasmrhomework.domain.entity

data class TodoListViewState(
    val result: Result<List<TodoItem>>,
    val errorMessageResourceId: Int
) {
    companion object {
        const val OK = -1
        val PLUG = TodoListViewState(
            Result(ResultStatus.LOADING, mutableListOf()),
            OK
        )
    }
}
