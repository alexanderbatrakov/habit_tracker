package com.example.myapplication.core.database.mapper



import org.junit.Test
import kotlin.test.assertEquals

class HabitMapperTest {

    @Test
    fun `habit mapper to domain test`() {
        val entity = HabitsFactory.habitEntity()
        val expected = HabitsFactory.getHabit()

        val actual = entity.toDomain()

        assertEquals(expected, actual)
    }
}
