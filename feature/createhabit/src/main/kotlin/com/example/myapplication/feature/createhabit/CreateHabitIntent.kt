package com.example.myapplication.feature.createhabit

import com.example.myapplication.domain.model.HabitStatus

sealed interface CreateHabitIntent {
    data class NameChanged(val value: String) : CreateHabitIntent
    data class DescriptionChanged(val value: String) : CreateHabitIntent
    data class StatusChanged(val value: HabitStatus) : CreateHabitIntent
    data class Prefilled(val name: String, val description: String) : CreateHabitIntent
    object Save : CreateHabitIntent
    data class SaveCompleted(val success: Boolean) : CreateHabitIntent
}
