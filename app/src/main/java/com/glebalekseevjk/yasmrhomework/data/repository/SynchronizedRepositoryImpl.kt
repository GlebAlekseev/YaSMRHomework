package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.SynchronizedRepository
import javax.inject.Inject

class SynchronizedRepositoryImpl @Inject constructor(context: Context) :
    SharedPreferencesSynchronizedStorage(context),
    SynchronizedRepository