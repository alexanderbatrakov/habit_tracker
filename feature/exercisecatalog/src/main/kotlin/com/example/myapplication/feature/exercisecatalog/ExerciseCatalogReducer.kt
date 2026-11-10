package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.core.common.ReduceResult

object ExerciseCatalogReducer {

    fun reduce(
        state: ExerciseCatalogState,
        intent: ExerciseCatalogIntent,
    ): ReduceResult<ExerciseCatalogState, ExerciseCatalogEffect> {
        return when (intent) {
            ExerciseCatalogIntent.InitialLoadRequested -> ReduceResult(
                state = state.copy(isLoading = true, isError = false, endReached = false),
                effect = ExerciseCatalogEffect.FetchFirstPage(state.selectedCategoryId),
            )

            ExerciseCatalogIntent.RetryClicked -> {
                if (state.exercises.isEmpty()) {
                    ReduceResult(
                        state = state.copy(isLoading = true, isError = false),
                        effect = ExerciseCatalogEffect.FetchFirstPage(state.selectedCategoryId),
                    )
                } else {
                    ReduceResult(
                        state = state.copy(isLoadingMore = true, isLoadMoreError = false),
                        effect = ExerciseCatalogEffect.FetchNextPage(
                            categoryId = state.selectedCategoryId,
                            offset = state.exercises.size,
                        ),
                    )
                }
            }

            is ExerciseCatalogIntent.CategorySelected -> {
                if (intent.categoryId == state.selectedCategoryId) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state.copy(
                            selectedCategoryId = intent.categoryId,
                            exercises = emptyList(),
                            total = 0,
                            isLoading = true,
                            isLoadingMore = false,
                            isError = false,
                            isLoadMoreError = false,
                            endReached = false,
                        ),
                        effect = ExerciseCatalogEffect.FetchFirstPage(intent.categoryId),
                    )
                }
            }

            ExerciseCatalogIntent.LoadMoreRequested -> {
                if (!state.canLoadMore && !state.isLoadMoreError) {
                    ReduceResult.just(state)
                } else {
                    ReduceResult(
                        state = state.copy(isLoadingMore = true, isLoadMoreError = false),
                        effect = ExerciseCatalogEffect.FetchNextPage(
                            categoryId = state.selectedCategoryId,
                            offset = state.exercises.size,
                        ),
                    )
                }
            }

            is ExerciseCatalogIntent.ExerciseClicked -> ReduceResult(
                state = state,
                effect = ExerciseCatalogEffect.CreateHabitRequested(intent.exercise),
            )

            is ExerciseCatalogIntent.InitialLoaded -> ReduceResult.just(
                state.copy(
                    categories = intent.categories,
                    exercises = intent.exercises,
                    total = intent.total,
                    isLoading = false,
                    isError = false,
                    endReached = !intent.hasMore,
                )
            )

            ExerciseCatalogIntent.InitialLoadFailed -> ReduceResult.just(
                state.copy(isLoading = false, isError = true)
            )

            is ExerciseCatalogIntent.NextPageLoaded -> ReduceResult.just(
                state.copy(
                    exercises = (state.exercises + intent.exercises).distinctBy { it.id },
                    total = intent.total,
                    isLoadingMore = false,
                    isLoadMoreError = false,
                    endReached = !intent.hasMore,
                )
            )

            ExerciseCatalogIntent.NextPageFailed -> ReduceResult.just(
                state.copy(isLoadingMore = false, isLoadMoreError = true)
            )
        }
    }
}
