package com.glebalekseevjk.yasmrhomework.domain.repository

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIsDarkTheme(): Boolean
    fun isDarkTheme(): Flow<Result<Boolean>>
    fun setDarkTheme(value: Boolean): Flow<Result<Unit>>
}