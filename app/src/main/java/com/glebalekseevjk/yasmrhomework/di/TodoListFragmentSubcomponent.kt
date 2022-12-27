package com.glebalekseevjk.yasmrhomework.di

import com.glebalekseevjk.yasmrhomework.di.module.ViewModelModule
import com.glebalekseevjk.yasmrhomework.di.scope.TodoListFragmentSubcomponentScope
import com.glebalekseevjk.yasmrhomework.ui.fragment.TodoListFragment
import dagger.Subcomponent

@TodoListFragmentSubcomponentScope
@Subcomponent
interface TodoListFragmentSubcomponent {
    fun inject(todoListFragment: TodoListFragment)
}