package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.domain.model.ExerciseCategory
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogIntent
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogReducer
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogState
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogEffect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExerciseCatalogReducerTest {

    private val exercise = Exercise(
        id = 1L,
        name = "Squat",
        description = "Legs exercise",
        categoryName = "Legs",
        muscles = listOf("Quads"),
        equipment = emptyList(),
    )

    @Test
    fun `initial load requests first page and sets loading`() {
        val state = ExerciseCatalogState()

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.InitialLoadRequested)

        assertTrue(result.state.isLoading)
        assertFalse(result.state.isError)
        assertEquals(ExerciseCatalogEffect.FetchFirstPage(null), result.effect)
    }

    @Test
    fun `initial loaded fills state and marks end when no more pages`() {
        val state = ExerciseCatalogState(isLoading = true)

        val result = ExerciseCatalogReducer.reduce(
            state,
            ExerciseCatalogIntent.InitialLoaded(
                categories = listOf(ExerciseCategory(1, "Legs")),
                exercises = listOf(exercise),
                total = 1,
                hasMore = false,
            ),
        )

        assertFalse(result.state.isLoading)
        assertFalse(result.state.isError)
        assertTrue(result.state.endReached)
        assertEquals(1, result.state.exercises.size)
    }

    @Test
    fun `category selection resets list and requests first page for category`() {
        val state = ExerciseCatalogState(
            exercises = listOf(exercise),
            endReached = true,
            isLoading = false,
        )

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.CategorySelected(9L))

        assertEquals(9L, result.state.selectedCategoryId)
        assertTrue(result.state.exercises.isEmpty())
        assertTrue(result.state.isLoading)
        assertFalse(result.state.endReached)
        assertEquals(ExerciseCatalogEffect.FetchFirstPage(9L), result.effect)
    }

    @Test
    fun `same category selection is ignored`() {
        val state = ExerciseCatalogState(selectedCategoryId = 9L)

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.CategorySelected(9L))

        assertNull(result.effect)
        assertEquals(state, result.state)
    }

    @Test
    fun `load more is ignored while end reached`() {
        val state = ExerciseCatalogState(
            exercises = listOf(exercise),
            endReached = true,
        )

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.LoadMoreRequested)

        assertNull(result.effect)
        assertFalse(result.state.isLoadingMore)
    }

    @Test
    fun `load more requested fetches next page with offset`() {
        val state = ExerciseCatalogState(
            exercises = listOf(exercise),
            selectedCategoryId = 9L,
            isLoading = false,
            endReached = false,
        )

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.LoadMoreRequested)

        assertTrue(result.state.isLoadingMore)
        assertEquals(ExerciseCatalogEffect.FetchNextPage(categoryId = 9L, offset = 1), result.effect)
    }

    @Test
    fun `next page loaded appends exercises and dedupes by id`() {
        val another = exercise.copy(id = 2L, name = "Lunge")
        val state = ExerciseCatalogState(exercises = listOf(exercise), isLoadingMore = true)

        val result = ExerciseCatalogReducer.reduce(
            state,
            ExerciseCatalogIntent.NextPageLoaded(
                exercises = listOf(another, exercise),
                total = 2,
                hasMore = false,
            ),
        )

        assertFalse(result.state.isLoadingMore)
        assertTrue(result.state.endReached)
        assertEquals(listOf(exercise, another), result.state.exercises)
    }

    @Test
    fun `load more retry works after failure`() {
        val state = ExerciseCatalogState(
            exercises = listOf(exercise),
            isLoadMoreError = true,
            endReached = false,
        )

        val result = ExerciseCatalogReducer.reduce(state, ExerciseCatalogIntent.RetryClicked)

        assertTrue(result.state.isLoadingMore)
        assertFalse(result.state.isLoadMoreError)
        assertEquals(ExerciseCatalogEffect.FetchNextPage(null, offset = 1), result.effect)
    }

    @Test
    fun `exercise clicked emits create habit effect`() {
        val state = ExerciseCatalogState(exercises = listOf(exercise))

        val result = ExerciseCatalogReducer.reduce(
            state,
            ExerciseCatalogIntent.ExerciseClicked(exercise),
        )

        assertEquals(ExerciseCatalogEffect.CreateHabitRequested(exercise), result.effect)
    }
}
