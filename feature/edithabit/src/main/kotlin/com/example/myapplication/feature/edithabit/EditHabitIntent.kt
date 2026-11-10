package com.example.myapplication.feature.edithabit

import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.Habit

sealed interface EditHabitIntent {
    data class HabitLoaded(val habit: Habit?) : EditHabitIntent
    data class NameChanged(val value: String) : EditHabitIntent
    data class DescriptionChanged(val value: String) : EditHabitIntent
    data class StatusChanged(val status: HabitStatus) : EditHabitIntent
    object Save : EditHabitIntent
    data class SaveCompleted(val success: Boolean) : EditHabitIntent
}
