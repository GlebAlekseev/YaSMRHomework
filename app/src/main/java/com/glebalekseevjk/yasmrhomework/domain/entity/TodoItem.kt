package com.glebalekseevjk.yasmrhomework.domain.entity

data class TodoItem(
    val userId: Long = UNDEFINED,
    val id: Long = UNDEFINED,
    val text: String,
    val importance: Importance = Importance.LOW,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String = "#FFFFFF",
    val createdAt: Long = System.currentTimeMillis(),
    val changedAt: Long? = null,
    val lastUpdatedBy: Long = UNDEFINED
) {
    companion object {
        const val UNDEFINED = 0L
        val PLUG = TodoItem(
            text = "plug",
            done = false
        )
        const val DAY_MILLIS = 86400000
    }
}
enum class Importance {
    LOW,
    BASIC,
    IMPORTANT
}