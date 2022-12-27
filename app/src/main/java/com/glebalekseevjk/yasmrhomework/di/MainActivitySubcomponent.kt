package com.glebalekseevjk.yasmrhomework.di

import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.di.scope.MainActivitySubcomponentScope
import com.glebalekseevjk.yasmrhomework.ui.activity.MainActivity
import dagger.Subcomponent
import javax.inject.Singleton

@MainActivitySubcomponentScope
@Subcomponent
interface MainActivitySubcomponent {
    fun inject(activity: MainActivity)
}
