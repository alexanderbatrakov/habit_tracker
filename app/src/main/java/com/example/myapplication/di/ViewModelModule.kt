package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.feature.createhabit.CreateHabitViewModel
import com.example.myapplication.feature.habitslist.HabitsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HabitsListViewModel::class)
    abstract fun bindHabitsListViewModel(vm: HabitsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateHabitViewModel::class)
    abstract fun bindCreateHabitViewModel(vm: CreateHabitViewModel): ViewModel
}
