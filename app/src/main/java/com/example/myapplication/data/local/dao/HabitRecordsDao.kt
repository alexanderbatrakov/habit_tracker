package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entity.HabitRecordsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitRecordsDao {

    @Insert
    suspend fun insert(habitEntity: HabitRecordsEntity): Long

    @Query("SELECT * FROM habits_records_entity")
    suspend fun getAll(): List<HabitRecordsEntity>

    @Query("SELECT * FROM habits_records_entity")
    fun observeAll(): Flow<List<HabitRecordsEntity>>

    @Query("SELECT * FROM habits_records_entity WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun observeForHabit(habitId: Long): Flow<List<HabitRecordsEntity>>

    @Query("SELECT * FROM habits_records_entity WHERE id = :id")
    suspend fun getById(id: Long): HabitRecordsEntity?

    @Query(
        "DELETE FROM habits_records_entity " +
            "WHERE habitId = :habitId AND completedAt >= :dayStart AND completedAt < :dayEnd"
    )
    suspend fun deleteForHabitOnDay(habitId: Long, dayStart: Long, dayEnd: Long)

    @Delete
    suspend fun delete(habit: HabitRecordsEntity)

    @Update
    suspend fun update(habit: HabitRecordsEntity)
}
