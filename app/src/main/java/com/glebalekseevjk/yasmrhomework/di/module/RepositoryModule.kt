package com.glebalekseevjk.yasmrhomework.di.module

import com.glebalekseevjk.yasmrhomework.data.repository.*
import com.glebalekseevjk.yasmrhomework.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    @Binds
    fun bindRevisionRepository(revisionRepositoryImpl: RevisionRepositoryImpl): RevisionRepository
    @Binds
    fun bindSchedulerRepository(schedulerRepositoryImpl: SchedulerRepositoryImpl): SchedulerRepository
    @Binds
    fun bindSynchronizedRepository(synchronizedRepositoryImpl: SynchronizedRepositoryImpl): SynchronizedRepository
    @Binds
    fun bindTodoListLocalRepository(todoListLocalRepositoryImpl: TodoListLocalRepositoryImpl): TodoListLocalRepository
    @Binds
    fun bindTodoListRemoteRepository(todoListRemoteRepositoryImpl: TodoListRemoteRepositoryImpl): TodoListRemoteRepository
    @Binds
    fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository
}