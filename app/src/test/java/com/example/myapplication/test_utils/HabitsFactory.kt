package com.example.myapplication.test_utils

import com.example.myapplication.data.local.entity.HabitEntity
import com.example.myapplication.data.local.entity.HabitStatus
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.feature.createhabit.CreateHabitState
import org.mockito.kotlin.description

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
            status = HabitStatus.CONTINUE,
            createAt = 123L
        )
    }

    fun createHabitState(): CreateHabitState {
        return CreateHabitState(
            name = "test name",
            description = "test description",
            status = HabitStatus.CONTINUE,
        )
    }
}