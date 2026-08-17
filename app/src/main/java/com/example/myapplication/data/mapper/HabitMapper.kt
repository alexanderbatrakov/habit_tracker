package com.example.myapplication.data.mapper

import com.example.myapplication.data.local.entity.HabitEntity
import com.example.myapplication.data.local.entity.HabitRecordsEntity
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.domain.model.HabitRecord

internal fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    description = description,
    status = status,
    createdAt = createAt,
)

internal fun List<HabitEntity>.toDomain(): List<Habit> = map { it.toDomain() }

internal fun HabitRecordsEntity.toDomain(): HabitRecord = HabitRecord(
    id = id,
    habitId = habitId,
    completedAt = completedAt,
)

internal fun List<HabitRecordsEntity>.toRecordDomain(): List<HabitRecord> = map { it.toDomain() }
