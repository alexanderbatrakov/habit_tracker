package com.example.myapplication.feature.edithabit

import com.example.myapplication.core.designsystem.R as DsR
import com.example.myapplication.core.common.ReduceResult

object EditHabitReducer {

    fun reduce(state: EditHabitState, intent: EditHabitIntent): ReduceResult<EditHabitState, EditHabitEffect> {
        return when (intent) {
            is EditHabitIntent.HabitLoaded -> {
                val habit = intent.habit
                if (habit == null) {
                    ReduceResult(state, EditHabitEffect.NavigateBack)
                } else {
                    ReduceResult.just(
                        state.copy(
                            habit = habit,
                            name = habit.name,
                            description = habit.description,
                            status = habit.status,
                        )
                    )
                }
            }

            is EditHabitIntent.NameChanged -> ReduceResult.just(
                state.copy(name = intent.value, nameErrorRes = null)
            )

            is EditHabitIntent.DescriptionChanged -> ReduceResult.just(
                state.copy(description = intent.value)
            )

            is EditHabitIntent.StatusChanged -> ReduceResult.just(
                state.copy(status = intent.status)
            )

            EditHabitIntent.Save -> {
                val habit = state.habit
                when {
                    !state.isNameValid -> ReduceResult(
                        state.copy(nameErrorRes = DsR.string.habit_name_error)
                    )

                    state.isSaving || habit == null -> ReduceResult.just(state)

                    else -> ReduceResult(
                        state = state.copy(isSaving = true, nameErrorRes = null),
                        effect = EditHabitEffect.SaveRequested(
                            habit.copy(
                                name = state.name.trim(),
                                description = state.description.trim(),
                                status = state.status,
                            ),
                        ),
                    )
                }
            }

            is EditHabitIntent.SaveCompleted -> {
                if (intent.success) {
                    ReduceResult(state.copy(isSaving = false), EditHabitEffect.Saved)
                } else {
                    ReduceResult(state.copy(isSaving = false))
                }
            }
        }
    }
}
