package com.example.myapplication.feature.habitslist

sealed interface HabitsListEffect {
    object NavigateToCreateHabit : HabitsListEffect
    data class ToggleDoneRequested(val habitId: Long, val markDone: Boolean) : HabitsListEffect
}
