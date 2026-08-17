package com.example.myapplication.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class ReducerStore<S, I : Any, E>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects: SharedFlow<E> = _effects.asSharedFlow()

    protected val currentState: S get() = _state.value

    fun dispatch(intent: I) {
        val result = reduce(currentState, intent)
        _state.value = result.state
        result.effect?.let { effect ->
            viewModelScope.launch { handleEffect(effect) }
        }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    protected abstract fun reduce(state: S, intent: I): ReduceResult<S, E>

    protected open suspend fun handleEffect(effect: E) {
        emitEffect(effect)
    }
}
