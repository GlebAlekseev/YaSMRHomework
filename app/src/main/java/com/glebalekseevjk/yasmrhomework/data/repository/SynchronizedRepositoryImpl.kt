package com.glebalekseevjk.yasmrhomework.data.repository

import android.content.Context
import com.glebalekseevjk.yasmrhomework.data.preferences.SharedPreferencesSynchronizedStorage
import com.glebalekseevjk.yasmrhomework.domain.repository.SynchronizedRepository

class SynchronizedRepositoryImpl(context: Context) : SharedPreferencesSynchronizedStorage(context),
    SynchronizedRepository