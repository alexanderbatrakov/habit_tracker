package com.example.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits_entity")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val status: HabitStatus,
    val createAt: Long,
)


enum class HabitStatus {
    CONTINUE,
    PAUSED,
    ARCHIVED
}