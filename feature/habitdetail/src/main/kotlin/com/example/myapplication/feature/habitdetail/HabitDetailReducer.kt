package com.example.myapplication.feature.habitdetail

import com.example.myapplication.core.common.ReduceResult

object HabitDetailReducer {

    fun reduce(state: HabitDetailState, intent: HabitDetailIntent): ReduceResult<HabitDetailState, HabitDetailEffect> {
        return when (intent) {
            is HabitDetailIntent.HabitLoaded -> {
                val progress = intent.progress
                if (progress == null) {
                    ReduceResult(
                        state = state.copy(
                            habit = null,
                            isLoading = false,
                            isDeleting = false,
                            isDeleteDialogVisible = false,
                        ),
                        effect = HabitDetailEffect.NavigateBack,
                    )
                } else {
                    ReduceResult.just(
                        state.copy(
                            habit = progress.habit,
                            streak = progress.streak,
                            isDoneToday = progress.isDoneToday,
                            isLoading = false,
                        )
                    )
                }
            }

            is HabitDetailIntent.StatusChanged -> {
                val habit = state.habit
                if (habit == null || habit.status == intent.status || state.isUpdating) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state.copy(isUpdating = true),
                        effect = HabitDetailEffect.StatusUpdateRequested(habit, intent.status),
                    )
                }
            }

            is HabitDetailIntent.StatusUpdateResult -> ReduceResult.just(state.copy(isUpdating = false))

            HabitDetailIntent.EditClicked -> {
                val habit = state.habit
                if (habit == null) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state,
                        effect = HabitDetailEffect.NavigateToEdit(habit.id),
                    )
                }
            }

            HabitDetailIntent.DeleteClicked -> ReduceResult.just(
                state.copy(isDeleteDialogVisible = true)
            )

            HabitDetailIntent.DeleteDialogDismissed -> ReduceResult.just(
                state.copy(isDeleteDialogVisible = false)
            )

            HabitDetailIntent.DeleteConfirmed -> {
                val habit = state.habit
                if (habit == null || state.isDeleting) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state.copy(isDeleting = true, isDeleteDialogVisible = false),
                        effect = HabitDetailEffect.DeleteRequested(habit),
                    )
                }
            }

            is HabitDetailIntent.DeleteResult -> ReduceResult.just(state.copy(isDeleting = false))
        }
    }
}
