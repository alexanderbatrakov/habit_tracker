package com.example.myapplication.core.designsystem

import com.example.myapplication.domain.model.HabitStatus

object TestTags {

    object HabitsList {
        const val ADD_HABIT_BUTTON = "habits_list_add_habit_button"

        fun statusTag(habitId: Long): String = "habits_list_status_$habitId"
    }

    object BottomBar {
        const val HABITS_TAB = "bottom_bar_habits_tab"
        const val CATALOG_TAB = "bottom_bar_catalog_tab"
    }

    object CreateHabit {
        const val TITLE = "create_habit_title"
        const val BACK_BUTTON = "create_habit_back_button"
        const val NAME_INPUT = "create_habit_name_input"
        const val DESCRIPTION_INPUT = "create_habit_description_input"
        const val STATUS_LABEL = "create_habit_status_label"
        const val SAVE_BUTTON = "create_habit_save_button"

        fun statusChip(status: HabitStatus): String = "create_habit_status_chip_${status.name}"
    }

    object HabitDetail {
        const val BACK_BUTTON = "habit_detail_back_button"
        const val NAME = "habit_detail_name"
        const val DESCRIPTION = "habit_detail_description"
        const val STATUS_LABEL = "habit_detail_status_label"
        const val STREAK = "habit_detail_streak"
        const val DELETE_BUTTON = "habit_detail_delete_button"
        const val DELETE_CONFIRM_BUTTON = "habit_detail_delete_confirm_button"
        const val DELETE_CANCEL_BUTTON = "habit_detail_delete_cancel_button"
        const val EDIT_BUTTON = "habit_detail_edit_button"

        fun statusChip(status: HabitStatus): String = "habit_detail_status_chip_${status.name}"
    }

    object EditHabit {
        const val TITLE = "edit_habit_title"
        const val BACK_BUTTON = "edit_habit_back_button"
        const val NAME_INPUT = "edit_habit_name_input"
        const val DESCRIPTION_INPUT = "edit_habit_description_input"
        const val STATUS_LABEL = "edit_habit_status_label"
        const val SAVE_BUTTON = "edit_habit_save_button"

        fun statusChip(status: HabitStatus): String = "edit_habit_status_chip_${status.name}"
    }

    object ExerciseCatalog {
        const val TITLE = "exercise_catalog_title"
        const val RETRY_BUTTON = "exercise_catalog_retry_button"
        const val LIST = "exercise_catalog_list"

        fun exerciseItem(id: Long): String = "exercise_catalog_item_$id"

        fun categoryChip(categoryId: Long?): String =
            if (categoryId == null) "exercise_catalog_category_all" else "exercise_catalog_category_$categoryId"
    }
}
