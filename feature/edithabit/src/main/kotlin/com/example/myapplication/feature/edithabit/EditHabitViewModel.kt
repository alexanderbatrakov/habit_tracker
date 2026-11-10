package com.example.myapplication.feature.edithabit

import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.HabitsRepository
import com.example.myapplication.core.common.ReduceResult
import com.example.myapplication.core.common.ReducerStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditHabitViewModel @Inject constructor(
    private val repository: HabitsRepository,
) : ReducerStore<EditHabitState, EditHabitIntent, EditHabitEffect>(EditHabitState()) {

    private var loadJob: Job? = null

    fun start(habitId: Long) {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            val habit = runCatching { repository.getHabit(habitId) }.getOrNull()
            dispatch(EditHabitIntent.HabitLoaded(habit))
        }
    }

    override fun reduce(
        state: EditHabitState,
        intent: EditHabitIntent,
    ): ReduceResult<EditHabitState, EditHabitEffect> = EditHabitReducer.reduce(state, intent)

    override suspend fun handleEffect(effect: EditHabitEffect) {
        when (effect) {
            is EditHabitEffect.SaveRequested -> {
                val success = runCatching {
                    repository.updateHabit(effect.habit)
                }.isSuccess
                dispatch(EditHabitIntent.SaveCompleted(success))
            }

            EditHabitEffect.Saved -> emitEffect(effect)
            EditHabitEffect.NavigateBack -> emitEffect(effect)
        }
    }
}
