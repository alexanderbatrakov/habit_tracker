package com.example.myapplication.ui.util

import com.example.myapplication.data.local.entity.HabitStatus

fun HabitStatus.displayName(): String = when (this) {
    HabitStatus.CONTINUE -> "Активна"
    HabitStatus.PAUSED -> "Пауза"
    HabitStatus.ARCHIVED -> "Архив"
}
