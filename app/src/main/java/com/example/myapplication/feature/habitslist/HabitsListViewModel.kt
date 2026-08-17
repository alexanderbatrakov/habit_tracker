package com.example.myapplication.feature.habitslist

import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.HabitsRepository
import com.example.myapplication.feature.ReduceResult
import com.example.myapplication.feature.ReducerStore
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitsListViewModel @Inject constructor(
    private val repository: HabitsRepository,
) : ReducerStore<HabitsListState, HabitsListIntent, HabitsListEffect>(HabitsListState()) {

    init {
        viewModelScope.launch {
            repository.observeHabitsWithProgress().collect { habits ->
                dispatch(HabitsListIntent.HabitsLoaded(habits))
            }
        }
    }

    override fun reduce(
        state: HabitsListState,
        intent: HabitsListIntent,
    ): ReduceResult<HabitsListState, HabitsListEffect> = HabitsListReducer.reduce(state, intent)

    override suspend fun handleEffect(effect: HabitsListEffect) {
        when (effect) {
            is HabitsListEffect.ToggleDoneRequested -> {
                val success = runCatching {
                    repository.setDoneToday(effect.habitId, effect.markDone)
                }.isSuccess
                dispatch(HabitsListIntent.ToggleDoneResult(effect.habitId, success))
            }

            HabitsListEffect.NavigateToCreateHabit -> emitEffect(effect)
        }
    }
}
