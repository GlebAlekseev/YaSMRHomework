package com.glebalekseevjk.yasmrhomework.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import javax.inject.Inject

open class SharedPreferencesTokenStorage @Inject constructor(context: Context) {
    private val tokenPref: SharedPreferences

    init {
        tokenPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getTokenPair(): TokenPair? {
        val expiresAt = getExpiresAt()
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        val login = getLogin()
        val displayName = getDisplayName()
        if (expiresAt != null && accessToken != null && refreshToken != null
            && login != null && displayName != null
        ) {
            return TokenPair(accessToken, refreshToken, expiresAt, login, displayName)
        } else {
            return null
        }
    }

    fun setTokenPair(tokenPair: TokenPair) {
        tokenPref.edit().putString(PREF_KEY_ACCESS_TOKEN, tokenPair.accessToken).apply()
        tokenPref.edit().putString(PREF_KEY_REFRESH_TOKEN, tokenPair.refreshToken).apply()
        tokenPref.edit().putLong(PREF_KEY_EXPIRES_AT, tokenPair.expiresAt).apply()
        tokenPref.edit().putString(PREF_KEY_LOGIN, tokenPair.login).apply()
        tokenPref.edit().putString(PREF_KEY_DISPLAY_NAME, tokenPair.displayName).apply()
    }

    fun clear() {
        tokenPref.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
        tokenPref.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
        tokenPref.edit().remove(PREF_KEY_EXPIRES_AT).apply()
        tokenPref.edit().remove(PREF_KEY_LOGIN).apply()
        tokenPref.edit().remove(PREF_KEY_DISPLAY_NAME).apply()
    }

    fun getExpiresAt(): Long? {
        val expiresAt = tokenPref.getLong(PREF_KEY_EXPIRES_AT, 0)
        if (expiresAt < System.currentTimeMillis()) {
            clear()
            return null
        }
        return expiresAt
    }

    fun getRefreshToken(): String? {
        val refresh = tokenPref.getString(PREF_KEY_REFRESH_TOKEN, "")
        return if (refresh != "") refresh else null
    }

    fun getAccessToken(): String? {
        val access = tokenPref.getString(PREF_KEY_ACCESS_TOKEN, "")
        return if (access != "") access else null
    }

    fun getLogin(): String? {
        val login = tokenPref.getString(PREF_KEY_LOGIN, "")
        return if (login != "") login else null
    }

    fun getDisplayName(): String? {
        val displayName = tokenPref.getString(PREF_KEY_DISPLAY_NAME, "")
        return if (displayName != "") displayName else null
    }


    companion object {
        private val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private val PREF_KEY_ACCESS_TOKEN = "access_token"
        private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
        private val PREF_KEY_EXPIRES_AT = "expires_at"
        private val PREF_KEY_LOGIN = "login"
        private val PREF_KEY_DISPLAY_NAME = "display_name"
    }
}