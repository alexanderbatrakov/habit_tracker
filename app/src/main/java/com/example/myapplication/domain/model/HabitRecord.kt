package com.example.myapplication.domain.model

data class HabitRecord(
    val id: Long,
    val habitId: Long,
    val completedAt: Long,
)

data class HabitWithProgress(
    val habit: Habit,
    val isDoneToday: Boolean,
    val streak: Int,
)
