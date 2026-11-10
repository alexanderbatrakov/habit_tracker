package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.ExerciseCategory
import com.example.myapplication.domain.model.ExercisePage

interface WgerRepository {

    suspend fun getCategories(): List<ExerciseCategory>

    suspend fun getExercises(categoryId: Long?, limit: Int, offset: Int): ExercisePage
}
