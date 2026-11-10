package com.example.myapplication.core.network.wger

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface WgerApi {

    @GET("api/v2/exercisecategory/")
    suspend fun getCategories(): WgerPageDto<CategoryDto>

    @GET("api/v2/exerciseinfo/")
    suspend fun getExercises(
        @Query("category") categoryId: Long? = null,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): WgerPageDto<ExerciseInfoDto>
}

@Serializable
data class WgerPageDto<T>(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T> = emptyList(),
)

@Serializable
data class CategoryDto(
    val id: Long,
    val name: String,
)

@Serializable
data class ExerciseInfoDto(
    val id: Long,
    val category: CategoryDto? = null,
    val muscles: List<MuscleDto> = emptyList(),
    val equipment: List<EquipmentDto> = emptyList(),
    val translations: List<ExerciseTranslationDto> = emptyList(),
)

@Serializable
data class MuscleDto(
    val id: Long,
    val name: String,
    @SerialName("name_en") val nameEn: String? = null,
)

@Serializable
data class EquipmentDto(
    val id: Long,
    val name: String,
)

@Serializable
data class ExerciseTranslationDto(
    val id: Long,
    val name: String,
    val language: Int,
    @SerialName("description_source") val descriptionSource: String? = null,
)
