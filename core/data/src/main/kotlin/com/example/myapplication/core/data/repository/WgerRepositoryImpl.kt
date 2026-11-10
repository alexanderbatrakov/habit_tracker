package com.example.myapplication.core.data.repository

import com.example.myapplication.core.network.wger.CategoryDto
import com.example.myapplication.core.network.wger.ExerciseInfoDto
import com.example.myapplication.core.network.wger.WgerApi
import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.domain.model.ExerciseCategory
import com.example.myapplication.domain.model.ExercisePage
import com.example.myapplication.domain.repository.WgerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WgerRepositoryImpl @Inject constructor(
    private val api: WgerApi,
) : WgerRepository {

    override suspend fun getCategories(): List<ExerciseCategory> =
        api.getCategories().results.map { it.toDomain() }

    override suspend fun getExercises(categoryId: Long?, limit: Int, offset: Int): ExercisePage {
        val page = api.getExercises(categoryId = categoryId, limit = limit, offset = offset)
        return ExercisePage(
            items = page.results.map { it.toDomain() },
            total = page.count,
            hasMore = page.next != null,
        )
    }

    private fun CategoryDto.toDomain(): ExerciseCategory = ExerciseCategory(
        id = id,
        name = name,
    )

    private fun ExerciseInfoDto.toDomain(): Exercise {
        val english = translations.firstOrNull { it.language == LANGUAGE_ENGLISH }
        val translation = english ?: translations.firstOrNull()
        return Exercise(
            id = id,
            name = translation?.name.orEmpty(),
            description = english?.descriptionSource?.takeIf { it.isNotBlank() }.orEmpty(),
            categoryName = category?.name.orEmpty(),
            muscles = muscles.mapNotNull { muscle -> muscle.nameEn ?: muscle.name },
            equipment = equipment.map { it.name },
        )
    }

    private companion object {
        const val LANGUAGE_ENGLISH = 2
    }
}
