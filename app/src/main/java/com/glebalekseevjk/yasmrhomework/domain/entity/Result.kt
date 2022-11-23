package com.glebalekseevjk.yasmrhomework.domain.entity

enum class ResultStatus {
    SUCCESS,        // Успех
    LOADING,        // Загрузка
    FAILURE,        // Ошибка
    UNAUTHORIZED    // Выбросить в окно авторизации
}

data class Result<T>(
    val status: ResultStatus,
    val data: T,
    val message: String = "",
)