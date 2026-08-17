package com.example.myapplication.data.local.entity

import androidx.room.TypeConverter

class HabitStatusConverter {

    @TypeConverter
    fun fromHabitStatus(value: HabitStatus): String {
       return value.name
    }

    @TypeConverter
    fun toHabitStatus(value: String): HabitStatus {
        return HabitStatus.valueOf(value)
    }
}