package com.glebalekseevjk.yasmrhomework.domain.entity.state
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem

data class TodoListViewState(
    val result: Result<List<TodoItem>>,
    val errorMessage: Int
){
    companion object{
        val DEFAULT = TodoListViewState(
            Result(ResultStatus.LOADING, mutableListOf()),
            -1
        )
    }
}
