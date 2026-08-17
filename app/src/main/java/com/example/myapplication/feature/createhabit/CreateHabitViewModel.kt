package com.example.myapplication.feature.createhabit

import com.example.myapplication.domain.repository.HabitsRepository
import com.example.myapplication.feature.ReduceResult
import com.example.myapplication.feature.ReducerStore
import javax.inject.Inject

class CreateHabitViewModel @Inject constructor(
    private val repository: HabitsRepository,
) : ReducerStore<CreateHabitState, CreateHabitIntent, CreateHabitEffect>(CreateHabitState()) {

    override fun reduce(
        state: CreateHabitState,
        intent: CreateHabitIntent,
    ): ReduceResult<CreateHabitState, CreateHabitEffect> = CreateHabitReducer.reduce(state, intent)

    override suspend fun handleEffect(effect: CreateHabitEffect) {
        when (effect) {
            is CreateHabitEffect.SaveRequested -> {
                val success = runCatching {
                    repository.addHabit(effect.name, effect.description, effect.status)
                }.isSuccess
                dispatch(CreateHabitIntent.SaveCompleted(success))
            }

            CreateHabitEffect.Saved -> emitEffect(effect)
        }
    }
}
