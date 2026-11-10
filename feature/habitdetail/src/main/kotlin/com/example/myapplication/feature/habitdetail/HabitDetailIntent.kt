package com.example.myapplication.feature.habitdetail

import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.domain.model.HabitWithProgress

sealed interface HabitDetailIntent {
    data class HabitLoaded(val progress: HabitWithProgress?) : HabitDetailIntent
    data class StatusChanged(val status: HabitStatus) : HabitDetailIntent
    data class StatusUpdateResult(val success: Boolean) : HabitDetailIntent
    object EditClicked : HabitDetailIntent
    object DeleteClicked : HabitDetailIntent
    object DeleteDialogDismissed : HabitDetailIntent
    object DeleteConfirmed : HabitDetailIntent
    data class DeleteResult(val success: Boolean) : HabitDetailIntent
}
