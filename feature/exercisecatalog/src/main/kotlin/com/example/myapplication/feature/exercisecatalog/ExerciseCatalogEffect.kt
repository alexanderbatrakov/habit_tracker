package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.domain.model.Exercise

sealed interface ExerciseCatalogEffect {
    data class FetchFirstPage(val categoryId: Long?) : ExerciseCatalogEffect
    data class FetchNextPage(val categoryId: Long?, val offset: Int) : ExerciseCatalogEffect
    data class CreateHabitRequested(val exercise: Exercise) : ExerciseCatalogEffect
}
