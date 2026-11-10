package com.example.myapplication.core.data.repository

import com.example.myapplication.core.database.entity.HabitEntity
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.domain.model.HabitStatus

object HabitsFactory {

    fun getHabit(): Habit {
        return Habit(
            id = 1L,
            name = "Test habit",
            description = "This is test habit",
            status = HabitStatus.CONTINUE,
            createdAt = 123L
        )
    }

    fun habitEntity(): HabitEntity {
        return HabitEntity(
            id = 1L,
            name = "Test habit",
            description = "This is test habit",
            status = HabitStatus.CONTINUE.name,
            createAt = 123L
        )
    }
}
