package com.example.myapplication.feature.edithabit

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.Habit

@Immutable
data class EditHabitState(
    val habit: Habit? = null,
    val name: String = "",
    val description: String = "",
    val status: HabitStatus = HabitStatus.CONTINUE,
    @StringRes val nameErrorRes: Int? = null,
    val isSaving: Boolean = false,
) {
    val isLoading: Boolean get() = habit == null
    val isNameValid: Boolean get() = name.trim().isNotEmpty()
    val canSave: Boolean get() = isNameValid && !isSaving
}
