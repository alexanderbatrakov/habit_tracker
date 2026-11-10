package com.example.myapplication.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object StreakCalculator {

    fun computeStreak(
        completedAtMillis: List<Long>,
        today: LocalDate = LocalDate.now(),
        zone: ZoneId = ZoneId.systemDefault(),
    ): Int {
        if (completedAtMillis.isEmpty()) return 0

        val days = completedAtMillis
            .map { Instant.ofEpochMilli(it).atZone(zone).toLocalDate() }
            .toSet()

        val streakStart = when {
            today in days -> today
            today.minusDays(1) in days -> today.minusDays(1)
            else -> return 0
        }

        var streak = 0
        var cursor = streakStart
        while (cursor in days) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }
}
