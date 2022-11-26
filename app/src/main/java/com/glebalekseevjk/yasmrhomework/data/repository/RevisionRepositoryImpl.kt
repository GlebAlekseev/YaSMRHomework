package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.RevisionRepository

class RevisionRepositoryImpl(context: Context) : SharedPreferencesRevisionStorage(context),
    RevisionRepository