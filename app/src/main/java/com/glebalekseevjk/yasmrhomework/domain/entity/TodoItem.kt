package com.glebalekseevjk.yasmrhomework.domain.entity

import java.time.LocalDateTime


data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val finished: Boolean,
    val created: LocalDateTime,
    val deadline: LocalDateTime? = null,
    val edited: LocalDateTime? = null
){
    companion object{
        enum class Importance{
            LOW,
            NORMAL,
            URGENT
        }
        val DEFAULT = TodoItem(
        "0",
        "",
        Importance.LOW,
        false,
        LocalDateTime.now(),
        null,
        null
        )
    }
}