package com.glebalekseevjk.yasmrhomework.di.module

import com.glebalekseevjk.yasmrhomework.data.repository.*
import com.glebalekseevjk.yasmrhomework.di.scope.AppComponentScope
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    @AppComponentScope
    @Binds
    fun bindRevisionRepository(revisionRepositoryImpl: RevisionRepositoryImpl): RevisionRepository
    @AppComponentScope
    @Binds
    fun bindSchedulerRepository(schedulerRepositoryImpl: SchedulerRepositoryImpl): SchedulerRepository
    @AppComponentScope
    @Binds
    fun bindSynchronizedRepository(synchronizedRepositoryImpl: SynchronizedRepositoryImpl): SynchronizedRepository
    @AppComponentScope
    @Binds
    fun bindTodoListLocalRepository(todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl): TodoListLocalRepository
    @AppComponentScope
    @Binds
    fun bindTodoListRemoteRepository(todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl): TodoListRemoteRepository
    @AppComponentScope
    @Binds
    fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository
    @AppComponentScope
    @Binds
    fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}