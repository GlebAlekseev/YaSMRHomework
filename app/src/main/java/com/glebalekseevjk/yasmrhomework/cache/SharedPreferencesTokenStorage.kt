package com.glebalekseevjk.yasmrhomework.cache

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.domain.feature.TokenStorage

class SharedPreferencesTokenStorage(context: Context) : TokenStorage {
    private val tokenPref: SharedPreferences

    init {
        tokenPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    override fun getTokenPair(): TokenPair? {
        val expiresAt = getExpiresAt()
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        if (expiresAt != null && accessToken != null && refreshToken != null) {
            return TokenPair(accessToken, refreshToken, expiresAt)
        } else {
            return null
        }
    }

    override fun setTokenPair(tokenPair: TokenPair) {
        tokenPref.edit().putString(PREF_KEY_ACCESS_TOKEN, tokenPair.accessToken).apply()
        tokenPref.edit().putString(PREF_KEY_REFRESH_TOKEN, tokenPair.refreshToken).apply()
        tokenPref.edit().putLong(PREF_KEY_EXPIRES_AT, tokenPair.expiresAt).apply()
    }

    override fun clear() {
        tokenPref.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
        tokenPref.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
        tokenPref.edit().remove(PREF_KEY_EXPIRES_AT).apply()
    }

    override fun getExpiresAt(): Long? {
        val expiresAt = tokenPref.getLong(PREF_KEY_EXPIRES_AT, 0)
        if (expiresAt < System.currentTimeMillis()) {
            clear()
            return null
        }
        return expiresAt
    }

    override fun getRefreshToken(): String? {
        val refresh = tokenPref.getString(PREF_KEY_REFRESH_TOKEN, "")
        return if (refresh != "") refresh else null
    }

    override fun getAccessToken(): String? {
        val access = tokenPref.getString(PREF_KEY_ACCESS_TOKEN, "")
        return if (access != "") access else null
    }

    companion object {
        private val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private val PREF_KEY_ACCESS_TOKEN = "access_token"
        private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
        private val PREF_KEY_EXPIRES_AT = "expires_at"
    }
}