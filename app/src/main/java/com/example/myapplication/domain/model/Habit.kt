package com.example.myapplication.domain.model

import com.example.myapplication.data.local.entity.HabitStatus

data class Habit(
    val id: Long,
    val name: String,
    val description: String,
    val status: HabitStatus,
    val createdAt: Long,
)
