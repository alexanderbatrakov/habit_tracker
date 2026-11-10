package com.example.myapplication

import com.example.myapplication.data.local.dao.HabitRecordsDao
import com.example.myapplication.data.local.dao.HabitsDao
import com.example.myapplication.data.local.entity.HabitStatus
import com.example.myapplication.data.repository.HabitsRepositoryImpl
import com.example.myapplication.domain.TimeProvider
import com.example.myapplication.test_utils.HabitsFactory
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
            //given:
            whenever(habitsDao.getById(any())).thenReturn(HabitsFactory.habitEntity())
            val expected = HabitsFactory.getHabit()

            //when:
            val actual = repository.getHabit(1L)

            //then:
            assertEquals(expected,actual)
        }
    }

    @Test
    fun `add habit test`() {
        runTest {
            //given:
            whenever(habitsDao.insert(any())).thenReturn(123L)

            //when:
            val actual = repository.addHabit(name = "test", description = "test", status = HabitStatus.CONTINUE)

            //then:
            assertEquals(123L, actual)
        }
    }
}

