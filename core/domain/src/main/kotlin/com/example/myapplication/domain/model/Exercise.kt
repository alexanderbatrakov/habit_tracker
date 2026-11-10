package com.example.myapplication.domain.model

data class ExerciseCategory(
    val id: Long,
    val name: String,
)

data class Exercise(
    val id: Long,
    val name: String,
    val description: String,
    val categoryName: String,
    val muscles: List<String>,
    val equipment: List<String>,
)

data class ExercisePage(
    val items: List<Exercise>,
    val total: Int,
    val hasMore: Boolean,
)
