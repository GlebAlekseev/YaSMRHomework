package com.glebalekseevjk.yasmrhomework.data.repository

import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesSettingsStorage
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsStorage: SharedPreferencesSettingsStorage,
) : SettingsRepository {
    private val _isDarkTheme: MutableStateFlow<Boolean> = MutableStateFlow(settingsStorage.getIsDarkTheme())
    override fun getIsDarkTheme(): Boolean {
        return _isDarkTheme.value
    }

    override fun isDarkTheme(): Flow<Result<Boolean>> = _isDarkTheme.map { Result(ResultStatus.SUCCESS, it) }

    override fun setDarkTheme(value: Boolean): Flow<Result<Unit>> = flow {
        emit(Result(ResultStatus.LOADING, Unit))
        settingsStorage.setIsDarkTheme(value)
        _isDarkTheme.emit(value)
        emit(Result(ResultStatus.SUCCESS, Unit))
    }.flowOn(Dispatchers.IO)

}