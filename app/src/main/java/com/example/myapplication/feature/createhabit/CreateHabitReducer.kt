package com.example.myapplication.feature.createhabit

import com.example.myapplication.R
import com.example.myapplication.feature.ReduceResult

object CreateHabitReducer {

    fun reduce(state: CreateHabitState, intent: CreateHabitIntent): ReduceResult<CreateHabitState, CreateHabitEffect> {
        return when (intent) {
            is CreateHabitIntent.NameChanged -> ReduceResult.just(
                state.copy(name = intent.value, nameErrorRes = null)
            )

            is CreateHabitIntent.DescriptionChanged -> ReduceResult.just(
                state.copy(description = intent.value)
            )

            is CreateHabitIntent.StatusChanged -> ReduceResult.just(
                state.copy(status = intent.value)
            )

            CreateHabitIntent.Save -> {
                if (!state.isNameValid) {
                    ReduceResult(state.copy(nameErrorRes = R.string.habit_name_error))
                } else if (state.isSaving) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state.copy(isSaving = true, nameErrorRes = null),
                        effect = CreateHabitEffect.SaveRequested(
                            name = state.name.trim(),
                            description = state.description.trim(),
                            status = state.status,
                        ),
                    )
                }
            }

            is CreateHabitIntent.SaveCompleted -> {
                if (intent.success) {
                    ReduceResult(state.copy(isSaving = false), CreateHabitEffect.Saved)
                } else {
                    ReduceResult(state.copy(isSaving = false))
                }
            }
        }
    }
}
