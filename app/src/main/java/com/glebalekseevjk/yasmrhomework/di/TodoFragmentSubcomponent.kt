package com.glebalekseevjk.yasmrhomework.di

import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.ui.fragment.TodoFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface TodoFragmentSubcomponent {
    fun inject(todoFragment: TodoFragment)
}