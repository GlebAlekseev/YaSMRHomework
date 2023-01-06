package com.glebalekseevjk.yasmrhomework.domain.interactor

import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.repository.SettingsRepository
import com.glebalekseevjk.yasmrhomework.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun setDarkTheme(value: Boolean): Flow<Result<Unit>> =
        settingsRepository.setDarkTheme(value)

    fun isDarkTheme(): Flow<Result<Boolean>> =
        settingsRepository.isDarkTheme()

    fun getIsDarkTheme(): Boolean = settingsRepository.getIsDarkTheme()
}