package com.example.myapplication.ui.navigation

import android.net.Uri

object Destinations {
    const val HABITS_LIST = "habits"

    const val ARG_HABIT_ID = "habitId"
    const val ARG_HABIT_NAME = "name"
    const val ARG_HABIT_DESCRIPTION = "description"

    const val CREATE_HABIT =
        "create_habit?$ARG_HABIT_NAME={$ARG_HABIT_NAME}&$ARG_HABIT_DESCRIPTION={$ARG_HABIT_DESCRIPTION}"
    const val EXERCISE_CATALOG = "exercise_catalog"
    const val HABIT_DETAIL = "habit_detail/{$ARG_HABIT_ID}"
    const val EDIT_HABIT = "edit_habit/{$ARG_HABIT_ID}"

    fun createHabit(name: String = "", description: String = ""): String =
        "create_habit?$ARG_HABIT_NAME=${Uri.encode(name)}&$ARG_HABIT_DESCRIPTION=${Uri.encode(description)}"

    fun habitDetail(habitId: Long): String = "habit_detail/$habitId"

    fun editHabit(habitId: Long): String = "edit_habit/$habitId"
}
