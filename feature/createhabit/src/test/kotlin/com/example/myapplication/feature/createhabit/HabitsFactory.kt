package com.example.myapplication.feature.createhabit

import com.example.myapplication.domain.model.HabitStatus

object HabitsFactory {

    fun createHabitState(): CreateHabitState {
        return CreateHabitState(
            name = "test name",
            description = "test description",
            status = HabitStatus.CONTINUE,
        )
    }
}
