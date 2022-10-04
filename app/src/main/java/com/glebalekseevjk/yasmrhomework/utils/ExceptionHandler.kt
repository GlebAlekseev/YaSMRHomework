package com.glebalekseevjk.yasmrhomework.utils

import androidx.annotation.StringRes
import com.glebalekseevjk.yasmrhomework.R
import java.lang.RuntimeException

internal object ExceptionHandler {
    @StringRes
    fun parse(t: Throwable): Int {
        return when (t) {
            is RuntimeException -> R.string.error_runtime_exception
            else -> R.string.error_oops
        }
    }
}
