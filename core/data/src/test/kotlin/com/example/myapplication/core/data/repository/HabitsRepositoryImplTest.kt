package com.example.myapplication.core.data.repository

import com.example.myapplication.core.database.dao.HabitRecordsDao
import com.example.myapplication.core.database.dao.HabitsDao
import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.core.data.repository.HabitsRepositoryImpl
import com.example.myapplication.domain.TimeProvider

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

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
}

