package com.example.myapplication.di

import com.example.myapplication.data.repository.HabitsRepositoryImpl
import com.example.myapplication.domain.repository.HabitsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHabitsRepository(impl: HabitsRepositoryImpl): HabitsRepository
}
