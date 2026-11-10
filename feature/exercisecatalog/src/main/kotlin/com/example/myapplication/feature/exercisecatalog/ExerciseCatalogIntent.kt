package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.domain.model.ExerciseCategory

sealed interface ExerciseCatalogIntent {
    object InitialLoadRequested : ExerciseCatalogIntent
    object RetryClicked : ExerciseCatalogIntent
    data class CategorySelected(val categoryId: Long?) : ExerciseCatalogIntent
    object LoadMoreRequested : ExerciseCatalogIntent
    data class ExerciseClicked(val exercise: Exercise) : ExerciseCatalogIntent

    data class InitialLoaded(
        val categories: List<ExerciseCategory>,
        val exercises: List<Exercise>,
        val total: Int,
        val hasMore: Boolean,
    ) : ExerciseCatalogIntent

    object InitialLoadFailed : ExerciseCatalogIntent
    data class NextPageLoaded(
        val exercises: List<Exercise>,
        val total: Int,
        val hasMore: Boolean,
    ) : ExerciseCatalogIntent

    object NextPageFailed : ExerciseCatalogIntent
}
