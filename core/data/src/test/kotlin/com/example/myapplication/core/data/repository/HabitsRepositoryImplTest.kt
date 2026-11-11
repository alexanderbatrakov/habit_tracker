package com.example.myapplication.core.data.repository

import com.example.myapplication.core.database.dao.HabitRecordsDao
import com.example.myapplication.core.database.dao.HabitsDao
import com.example.myapplication.core.database.entity.HabitRecordsEntity
import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.HabitWithProgress
import com.example.myapplication.domain.TimeProvider

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class HabitsRepositoryImplTest {
    private val habitsDao: HabitsDao = mock()
    private val recordsDao: HabitRecordsDao = mock()
    private val timeProvider: TimeProvider = mock()

        private val repository = HabitsRepositoryImpl(
        habitsDao = habitsDao,
        recordsDao = recordsDao,
        timeProvider = timeProvider
    )


    @Test
    fun `get habit test`() {
        runTest {
            whenever(habitsDao.getById(any())).thenReturn(HabitsFactory.habitEntity())
            val expected = HabitsFactory.getHabit()

            val actual = repository.getHabit(1L)

            assertEquals(expected,actual)
        }
    }

    @Test
    fun `add habit test`() {
        runTest {
            whenever(habitsDao.insert(any())).thenReturn(123L)

            val actual = repository.addHabit(name = "test", description = "test", status = HabitStatus.CONTINUE)

            assertEquals(123L, actual)
        }
    }

    @Test
    fun `set done today twice inserts single record`() {
        runTest {
            val record = HabitRecordsEntity(habitId = 1L, completedAt = 1L)
            whenever(recordsDao.getForHabitOnDay(any(), any(), any()))
                .thenReturn(emptyList(), listOf(record))

            repository.setDoneToday(1L, true)
            repository.setDoneToday(1L, true)

            Mockito.verify(recordsDao, Mockito.times(1)).insert(any())
        }
    }

    @Test
    fun `done today refreshed after midnight`() {
        runTest {
            val zone = ZoneId.systemDefault()
            var now = LocalDate.of(2026, 1, 15).atTime(23, 0).atZone(zone).toInstant().toEpochMilli()
            whenever(timeProvider.nowMillis()).thenAnswer { now }
            whenever(habitsDao.observeAll()).thenReturn(MutableStateFlow(listOf(HabitsFactory.habitEntity())))
            whenever(recordsDao.observeAll()).thenReturn(
                MutableStateFlow(listOf(HabitRecordsEntity(id = 1L, habitId = 1L, completedAt = now)))
            )

            val collected = mutableListOf<List<HabitWithProgress>>()
            val job = launch { repository.observeHabitsWithProgress().take(2).toList(collected) }
            runCurrent()
            now += 2 * MILLIS_PER_HOUR
            advanceTimeBy(2 * MILLIS_PER_HOUR)
            runCurrent()
            job.join()

            assertTrue(collected.first().first().isDoneToday)
            assertFalse(collected.last().first().isDoneToday)
        }
    }

    private companion object {
        const val MILLIS_PER_HOUR = 3_600_000L
    }
}

