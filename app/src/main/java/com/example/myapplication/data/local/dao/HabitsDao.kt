package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.HabitEntity


@Dao
interface HabitsDao {

    @Insert
    suspend fun insert(habitEntity: HabitEntity)

    @Query("SELECT * FROM habits_entity")
    suspend fun getAll(): List<HabitEntity>

    @Query("SELECT * FROM habits_entity WHERE id = :id")
    suspend fun getById(id: Long): HabitEntity?

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)
}