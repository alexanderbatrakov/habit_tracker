package com.example.myapplication.feature.createhabit

import com.example.myapplication.data.local.entity.HabitStatus

sealed interface CreateHabitEffect {
    data class SaveRequested(
        val name: String,
        val description: String,
        val status: HabitStatus,
    ) : CreateHabitEffect

    object Saved : CreateHabitEffect
}
