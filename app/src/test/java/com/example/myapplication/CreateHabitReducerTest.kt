package com.example.myapplication

import com.example.myapplication.data.local.entity.HabitStatus
import com.example.myapplication.feature.createhabit.CreateHabitIntent
import com.example.myapplication.feature.createhabit.CreateHabitReducer
import com.example.myapplication.test_utils.HabitsFactory
import org.junit.Test
import kotlin.test.assertTrue

class CreateHabitReducerTest {

    @Test
    fun `habit reducer status changed`() {
        //given:
        val state = HabitsFactory.createHabitState()

        //when:
        val actual = CreateHabitReducer.reduce(state, CreateHabitIntent.StatusChanged(HabitStatus.ARCHIVED))

        //then
        assertTrue(actual.state.status == HabitStatus.ARCHIVED)
    }
}