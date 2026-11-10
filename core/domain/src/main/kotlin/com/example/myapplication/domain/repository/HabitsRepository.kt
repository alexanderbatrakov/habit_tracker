package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.domain.model.HabitWithProgress
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun observeHabits(): Flow<List<Habit>>

    fun observeHabitsWithProgress(): Flow<List<HabitWithProgress>>

    fun observeHabitWithProgress(id: Long): Flow<HabitWithProgress?>

    suspend fun getHabit(id: Long): Habit?

    suspend fun addHabit(name: String, description: String, status: HabitStatus): Long

    suspend fun deleteHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun setDoneToday(habitId: Long, done: Boolean)
}
