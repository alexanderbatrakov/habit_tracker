package com.example.myapplication.feature.habitdetail

import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.HabitsRepository
import com.example.myapplication.core.common.ReduceResult
import com.example.myapplication.core.common.ReducerStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitDetailViewModel @Inject constructor(
    private val repository: HabitsRepository,
) : ReducerStore<HabitDetailState, HabitDetailIntent, HabitDetailEffect>(HabitDetailState()) {

    private var observeJob: Job? = null

    fun start(habitId: Long) {
        if (observeJob?.isActive == true) return
        observeJob = viewModelScope.launch {
            repository.observeHabitWithProgress(habitId).collect { progress ->
                dispatch(HabitDetailIntent.HabitLoaded(progress))
            }
        }
    }

    override fun reduce(
        state: HabitDetailState,
        intent: HabitDetailIntent,
    ): ReduceResult<HabitDetailState, HabitDetailEffect> = HabitDetailReducer.reduce(state, intent)

    override suspend fun handleEffect(effect: HabitDetailEffect) {
        when (effect) {
            is HabitDetailEffect.StatusUpdateRequested -> {
                val success = runCatching {
                    repository.updateHabit(effect.habit.copy(status = effect.status))
                }.isSuccess
                dispatch(HabitDetailIntent.StatusUpdateResult(success))
            }

            is HabitDetailEffect.DeleteRequested -> {
                val success = runCatching {
                    repository.deleteHabit(effect.habit)
                }.isSuccess
                dispatch(HabitDetailIntent.DeleteResult(success))
            }

            HabitDetailEffect.NavigateBack -> emitEffect(effect)
            is HabitDetailEffect.NavigateToEdit -> emitEffect(effect)
        }
    }
}
