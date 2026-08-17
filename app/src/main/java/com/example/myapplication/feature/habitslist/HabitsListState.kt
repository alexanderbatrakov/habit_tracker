package com.example.myapplication.feature.habitslist

import androidx.compose.runtime.Immutable
import com.example.myapplication.domain.model.HabitWithProgress

@Immutable
data class HabitsListState(
    val habits: List<HabitWithProgress> = emptyList(),
    val isLoading: Boolean = true,
)
