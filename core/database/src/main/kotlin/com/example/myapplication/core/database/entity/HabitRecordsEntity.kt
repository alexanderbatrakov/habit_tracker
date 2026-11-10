package com.example.myapplication.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits_records_entity")
data class HabitRecordsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Long
)
