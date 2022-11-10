package com.glebalekseevjk.yasmrhomework.cache

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.yasmrhomework.domain.entity.Revision
import com.glebalekseevjk.yasmrhomework.domain.features.revision.RevisionStorage

class SharedPreferencesRevisionStorage(context: Context) : RevisionStorage {
    private val revisionPref: SharedPreferences

    init {
        revisionPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    override fun getRevision(): Revision? {
        val revision = revisionPref.getLong(PREF_KEY_REVISION, 0)
        if (revision == 0L) {
            return null
        } else {
            return Revision(revision)
        }
    }

    override fun setRevision(revision: Revision) {
        revisionPref.edit().putLong(PREF_KEY_REVISION, revision.revision).apply()
    }

    override fun clear() {
        revisionPref.edit().remove(PREF_KEY_REVISION).apply()
    }

    companion object {
        private val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private val PREF_KEY_REVISION = "revision"
    }
}