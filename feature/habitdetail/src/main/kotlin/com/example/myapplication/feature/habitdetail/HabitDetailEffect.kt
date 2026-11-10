package com.example.myapplication.feature.habitdetail

import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.Habit

sealed interface HabitDetailEffect {
    data class StatusUpdateRequested(val habit: Habit, val status: HabitStatus) : HabitDetailEffect
    data class DeleteRequested(val habit: Habit) : HabitDetailEffect
    data class NavigateToEdit(val habitId: Long) : HabitDetailEffect
    object NavigateBack : HabitDetailEffect
}
