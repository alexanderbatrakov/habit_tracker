package com.example.myapplication.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.core.database.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Insert
    suspend fun insert(habitEntity: HabitEntity): Long

    @Query("SELECT * FROM habits_entity")
    fun observeAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits_entity")
    suspend fun getAll(): List<HabitEntity>

    @Query("SELECT * FROM habits_entity WHERE id = :id")
    suspend fun getById(id: Long): HabitEntity?

    @Query("SELECT * FROM habits_entity WHERE id = :id")
    fun observeById(id: Long): Flow<HabitEntity?>

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)
}
