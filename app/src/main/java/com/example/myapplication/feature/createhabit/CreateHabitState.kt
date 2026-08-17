package com.example.myapplication.feature.createhabit

import androidx.compose.runtime.Immutable
import com.example.myapplication.data.local.entity.HabitStatus

@Immutable
data class CreateHabitState(
    val name: String = "",
    val description: String = "",
    val status: HabitStatus = HabitStatus.CONTINUE,
    val nameError: String? = null,
    val isSaving: Boolean = false,
) {
    val isNameValid: Boolean get() = name.trim().isNotEmpty()
    val canSave: Boolean get() = isNameValid && !isSaving
}
