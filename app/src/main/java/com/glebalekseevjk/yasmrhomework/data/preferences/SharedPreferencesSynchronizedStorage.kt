package com.glebalekseevjk.yasmrhomework.data.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class SharedPreferencesSynchronizedStorage @Inject constructor(context: Context) {
    private val syncPref: SharedPreferences

    init {
        syncPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getSynchronizedStatus(): Boolean {
        return syncPref.getBoolean(PREF_KEY_SYNCHRONIZED_STATUS, SYNCHRONIZED)
    }

    fun setSynchronizedStatus(synchronizedStatus: Boolean) {
        syncPref.edit().putBoolean(PREF_KEY_SYNCHRONIZED_STATUS, synchronizedStatus).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private const val PREF_KEY_SYNCHRONIZED_STATUS = "synchronized_status"

        const val SYNCHRONIZED = true
        const val UNSYNCHRONIZED = false
    }
}