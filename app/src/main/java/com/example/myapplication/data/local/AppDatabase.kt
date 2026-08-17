package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.local.dao.HabitRecordsDao
import com.example.myapplication.data.local.dao.HabitsDao
import com.example.myapplication.data.local.entity.HabitEntity
import com.example.myapplication.data.local.entity.HabitRecordsEntity
import com.example.myapplication.data.local.entity.HabitStatusConverter

@Database(
    entities = [
        HabitEntity::class,
        HabitRecordsEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(HabitStatusConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun habitsDao(): HabitsDao
    abstract fun habitRecordsDao(): HabitRecordsDao
}