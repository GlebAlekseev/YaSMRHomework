package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(context: Context) :
    SharedPreferencesTokenStorage(context),
    TokenRepository