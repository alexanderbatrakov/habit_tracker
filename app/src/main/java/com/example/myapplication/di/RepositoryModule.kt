package com.example.myapplication.di

import com.example.myapplication.core.data.repository.HabitsRepositoryImpl
import com.example.myapplication.core.data.repository.WgerRepositoryImpl
import com.example.myapplication.domain.repository.HabitsRepository
import com.example.myapplication.domain.repository.WgerRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitsRepository(impl: HabitsRepositoryImpl): HabitsRepository

    @Binds
    @Singleton
    abstract fun bindWgerRepository(impl: WgerRepositoryImpl): WgerRepository
}
