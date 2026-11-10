package com.example.myapplication.feature.createhabit

import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.feature.createhabit.CreateHabitIntent
import com.example.myapplication.feature.createhabit.CreateHabitReducer

import org.junit.Test
import kotlin.test.assertTrue

class CreateHabitReducerTest {

    @Test
    fun `habit reducer status changed`() {
        val state = HabitsFactory.createHabitState()

        val actual = CreateHabitReducer.reduce(state, CreateHabitIntent.StatusChanged(HabitStatus.ARCHIVED))

        assertTrue(actual.state.status == HabitStatus.ARCHIVED)
    }
}
