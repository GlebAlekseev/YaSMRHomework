package com.glebalekseevjk.yasmrhomework.ui.viewmodel.state

data class MainState(
    val isAuth: Boolean = false,
    val login: String = "Unknown",
    val displayName: String = "Unknown",
    val isDarkMode: Boolean = false,
)