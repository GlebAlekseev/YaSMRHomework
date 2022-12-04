package com.glebalekseevjk.yasmrhomework.di

import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.ui.fragment.TodoListFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface TodoListFragmentSubcomponent {
    fun inject(todoListFragment: TodoListFragment)
}