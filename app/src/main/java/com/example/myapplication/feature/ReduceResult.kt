package com.example.myapplication.feature

data class ReduceResult<out S, out E>(
    val state: S,
    val effect: E? = null,
) {
    companion object {
        fun <S, E> just(state: S): ReduceResult<S, E> = ReduceResult(state)
    }
}
