package com.example.myapplication.feature.habitdetail

import androidx.compose.runtime.Immutable
import com.example.myapplication.domain.model.Habit

@Immutable
data class HabitDetailState(
    val habit: Habit? = null,
    val streak: Int = 0,
    val isDoneToday: Boolean = false,
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isUpdating: Boolean = false,
)
