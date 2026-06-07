package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.HabitRecordsEntity

@Dao
interface HabitRecordsDao {

    @Insert
    suspend fun insert(habitEntity: HabitRecordsEntity)

    @Query("SELECT * FROM habits_records_entity")
    suspend fun getAll(): List<HabitRecordsEntity>

    @Query("SELECT * FROM habits_records_entity WHERE id = :id")
    suspend fun getById(id: Long): HabitRecordsEntity?

    @Delete
    suspend fun delete(habit: HabitRecordsEntity)

    @Update
    suspend fun update(habit: HabitRecordsEntity)
}