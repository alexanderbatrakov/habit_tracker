package com.example.myapplication.feature.edithabit

import com.example.myapplication.domain.model.Habit

sealed interface EditHabitEffect {
    data class SaveRequested(val habit: Habit) : EditHabitEffect
    object Saved : EditHabitEffect
    object NavigateBack : EditHabitEffect
}
