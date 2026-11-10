package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import com.example.myapplication.feature.createhabit.CreateHabitViewModel
import com.example.myapplication.feature.edithabit.EditHabitViewModel
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogViewModel
import com.example.myapplication.feature.habitdetail.HabitDetailViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(HabitDetailViewModel::class)
    abstract fun bindHabitDetailViewModel(vm: HabitDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditHabitViewModel::class)
    abstract fun bindEditHabitViewModel(vm: EditHabitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExerciseCatalogViewModel::class)
    abstract fun bindExerciseCatalogViewModel(vm: ExerciseCatalogViewModel): ViewModel
}
