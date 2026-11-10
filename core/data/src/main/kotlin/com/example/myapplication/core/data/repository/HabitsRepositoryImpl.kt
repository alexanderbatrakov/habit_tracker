package com.example.myapplication.core.data.repository

import com.example.myapplication.core.database.dao.HabitRecordsDao
import com.example.myapplication.core.database.dao.HabitsDao
import com.example.myapplication.core.database.entity.HabitEntity
import com.example.myapplication.core.database.entity.HabitRecordsEntity
import com.example.myapplication.core.database.mapper.toDomain
import com.example.myapplication.core.database.mapper.toEntity
import com.example.myapplication.core.database.mapper.toRecordDomain
import com.example.myapplication.domain.StreakCalculator
import com.example.myapplication.domain.TimeProvider
import com.example.myapplication.domain.model.Habit
import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.HabitWithProgress
import com.example.myapplication.domain.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepositoryImpl @Inject constructor(
    private val habitsDao: HabitsDao,
    private val recordsDao: HabitRecordsDao,
    private val timeProvider: TimeProvider,
) : HabitsRepository {

    private val zone: ZoneId = ZoneId.systemDefault()

    override fun observeHabits(): Flow<List<Habit>> =
        habitsDao.observeAll().map { entities -> entities.toDomain() }

    override fun observeHabitsWithProgress(): Flow<List<HabitWithProgress>> =
        combine(habitsDao.observeAll(), recordsDao.observeAll()) { habits, records ->
            val today = currentToday()
            habits.map { habit ->
                val habitRecords = records.filter { it.habitId == habit.id }
                val isDoneToday = habitRecords.any { it.isToday(today) }
                val streak = StreakCalculator.computeStreak(
                    completedAtMillis = habitRecords.map { it.completedAt },
                    today = today,
                    zone = zone,
                )
                HabitWithProgress(
                    habit = habit.toDomain(),
                    isDoneToday = isDoneToday,
                    streak = streak,
                )
            }
        }

    override fun observeHabitWithProgress(id: Long): Flow<HabitWithProgress?> =
        combine(
            habitsDao.observeById(id),
            recordsDao.observeForHabit(id),
        ) { habit, records ->
            if (habit == null) {
                null
            } else {
                val today = currentToday()
                val isDoneToday = records.any { it.isToday(today) }
                val streak = StreakCalculator.computeStreak(
                    completedAtMillis = records.map { it.completedAt },
                    today = today,
                    zone = zone,
                )
                HabitWithProgress(
                    habit = habit.toDomain(),
                    isDoneToday = isDoneToday,
                    streak = streak,
                )
            }
        }

    override suspend fun getHabit(id: Long): Habit? =
        habitsDao.getById(id)?.toDomain()

    override suspend fun addHabit(
        name: String,
        description: String,
        status: HabitStatus,
    ): Long {
        val entity = HabitEntity(
            name = name,
            description = description,
            status = status.name,
            createAt = timeProvider.nowMillis(),
        )
        return habitsDao.insert(entity)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitsDao.delete(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitsDao.update(habit.toEntity())
    }

    override suspend fun setDoneToday(habitId: Long, done: Boolean) {
        val today = currentToday()
        val dayStart = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val dayEnd = dayStart + MILLIS_PER_DAY
        if (done) {
            recordsDao.insert(
                HabitRecordsEntity(
                    habitId = habitId,
                    completedAt = timeProvider.nowMillis(),
                )
            )
        } else {
            recordsDao.deleteForHabitOnDay(habitId, dayStart, dayEnd)
        }
    }

    private fun currentToday(): LocalDate =
        Instant.ofEpochMilli(timeProvider.nowMillis()).atZone(zone).toLocalDate()

    private fun HabitRecordsEntity.isToday(today: LocalDate): Boolean =
        Instant.ofEpochMilli(completedAt).atZone(zone).toLocalDate() == today

    private companion object {
        const val MILLIS_PER_DAY = 86_400_000L
    }
}
