package com.glebalekseevjk.yasmrhomework.di.module

import androidx.lifecycle.ViewModel
import com.glebalekseevjk.yasmrhomework.di.FromViewModelFactory
import com.glebalekseevjk.yasmrhomework.di.ViewModelKey
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.MainViewModel
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoListViewModel
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(TodoListViewModel::class)]
    fun bindTodoListViewModel(todoListViewModel: TodoListViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(TodoViewModel::class)]
    fun bindTodoViewModel(todoViewModel: TodoViewModel): ViewModel

    companion object {
        @FromViewModelFactory
        @Provides
        fun provideMainViewModel(viewModelFactory: ViewModelFactory): MainViewModel {
            return viewModelFactory.create(MainViewModel::class.java)
        }

        @FromViewModelFactory
        @Provides
        fun provideTodoListViewModel(viewModelFactory: ViewModelFactory): TodoListViewModel {
            return viewModelFactory.create(TodoListViewModel::class.java)
        }

        @FromViewModelFactory
        @Provides
        fun provideTodoViewModel(viewModelFactory: ViewModelFactory): TodoViewModel {
            return viewModelFactory.create(TodoViewModel::class.java)
        }
    }
}