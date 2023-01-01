package com.glebalekseevjk.yasmrhomework.utils

import android.content.Context
import com.glebalekseevjk.yasmrhomework.di.AppComponent
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication

val Context.appComponent: AppComponent
    get() = when(this){
        is MainApplication -> {
            appComponent
        }
        else -> {
            this.applicationContext.appComponent
        }
    }