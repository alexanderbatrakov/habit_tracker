package com.example.myapplication.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.core.database.dao.HabitRecordsDao
import com.example.myapplication.core.database.dao.HabitsDao
import com.example.myapplication.core.database.entity.HabitEntity
import com.example.myapplication.core.database.entity.HabitRecordsEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitRecordsEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitsDao(): HabitsDao

    abstract fun habitRecordsDao(): HabitRecordsDao
}
