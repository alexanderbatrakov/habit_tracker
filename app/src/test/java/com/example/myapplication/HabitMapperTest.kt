package com.example.myapplication

import com.example.myapplication.data.local.dao.HabitsDao
import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.test_utils.HabitsFactory
import org.junit.Test
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

class HabitMapperTest {

    private val habitsDao: HabitsDao = mock()

    @Test
    fun `habit mapper to domain test`() {
        //given:
        val entity = HabitsFactory.habitEntity()
        val expected = HabitsFactory.getHabit()

        //when:
        val actual = entity.toDomain()

        assertEquals(expected, actual)
    }
}