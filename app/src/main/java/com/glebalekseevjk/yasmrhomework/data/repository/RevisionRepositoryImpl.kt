package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.RevisionRepository
import javax.inject.Inject

class RevisionRepositoryImpl @Inject constructor(context: Context) :
    SharedPreferencesRevisionStorage(context),
    RevisionRepository