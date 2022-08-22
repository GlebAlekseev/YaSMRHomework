package com.glebalekseevjk.yasmrhomework.presentation.application

import android.app.Application
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel

class MainApplication: Application() {
    val mainViewModel: MainViewModel = MainViewModel()
    override fun onCreate() {
        super.onCreate()
    }
}