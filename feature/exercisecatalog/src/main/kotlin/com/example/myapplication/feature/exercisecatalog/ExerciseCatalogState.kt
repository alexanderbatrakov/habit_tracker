package com.example.myapplication.feature.exercisecatalog

import androidx.compose.runtime.Immutable
import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.domain.model.ExerciseCategory

@Immutable
data class ExerciseCatalogState(
    val categories: List<ExerciseCategory> = emptyList(),
    val selectedCategoryId: Long? = null,
    val exercises: List<Exercise> = emptyList(),
    val total: Int = 0,
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val isError: Boolean = false,
    val isLoadMoreError: Boolean = false,
    val endReached: Boolean = false,
) {
    val canLoadMore: Boolean
        get() = exercises.isNotEmpty() && !endReached && !isLoadingMore && !isLoading
}
