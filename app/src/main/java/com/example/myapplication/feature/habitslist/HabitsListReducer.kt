package com.example.myapplication.feature.habitslist

import com.example.myapplication.feature.ReduceResult

object HabitsListReducer {
    fun reduce(state: HabitsListState, intent: HabitsListIntent): ReduceResult<HabitsListState, HabitsListEffect> {
        return when (intent) {
            is HabitsListIntent.HabitsLoaded -> ReduceResult.just(
                state.copy(habits = intent.habits, isLoading = false)
            )

            HabitsListIntent.CreateHabitClicked -> ReduceResult(
                state = state,
                effect = HabitsListEffect.NavigateToCreateHabit,
            )

            is HabitsListIntent.ToggleDoneToday -> {
                val current = state.habits.firstOrNull { it.habit.id == intent.habitId }
                val markDone = current?.isDoneToday?.not() ?: true
                ReduceResult(
                    state = state,
                    effect = HabitsListEffect.ToggleDoneRequested(intent.habitId, markDone),
                )
            }

            is HabitsListIntent.ToggleDoneResult -> ReduceResult.just(state)
        }
    }
}
