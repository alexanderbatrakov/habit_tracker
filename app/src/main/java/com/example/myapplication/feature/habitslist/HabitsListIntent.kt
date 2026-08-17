package com.example.myapplication.feature.habitslist

sealed interface HabitsListIntent {
    object CreateHabitClicked : HabitsListIntent
    data class HabitsLoaded(val habits: List<com.example.myapplication.domain.model.HabitWithProgress>) : HabitsListIntent
    data class ToggleDoneToday(val habitId: Long) : HabitsListIntent
    data class ToggleDoneResult(val habitId: Long, val success: Boolean) : HabitsListIntent
}
