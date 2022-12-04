package com.glebalekseevjk.yasmrhomework.di

import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.ui.activity.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface MainActivitySubcomponent {
    fun inject(activity: MainActivity)
}

