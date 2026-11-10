package com.example.myapplication.core.database.mapper

import com.example.myapplication.core.database.entity.HabitEntity
import com.example.myapplication.core.database.entity.HabitRecordsEntity
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.domain.model.HabitRecord
import com.example.myapplication.domain.model.HabitStatus

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    description = description,
    status = HabitStatus.valueOf(status),
    createdAt = createAt,
)

fun List<HabitEntity>.toDomain(): List<Habit> = map { it.toDomain() }

fun HabitRecordsEntity.toDomain(): HabitRecord = HabitRecord(
    id = id,
    habitId = habitId,
    completedAt = completedAt,
)

fun List<HabitRecordsEntity>.toRecordDomain(): List<HabitRecord> = map { it.toDomain() }

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    description = description,
    status = status.name,
    createAt = createdAt,
)
