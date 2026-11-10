package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.domain.model.ExerciseCategory
import com.example.myapplication.domain.model.ExercisePage
import com.example.myapplication.domain.repository.WgerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseCatalogViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val repository: WgerRepository = mock()

    private val exercise = Exercise(
        id = 1L,
        name = "Squat",
        description = "Legs exercise",
        categoryName = "Legs",
        muscles = listOf("Quads"),
        equipment = emptyList(),
    )

    private lateinit var viewModel: ExerciseCatalogViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = ExerciseCatalogViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `start loads categories and first page`() = runTest {
        whenever(repository.getCategories()).thenReturn(listOf(ExerciseCategory(9L, "Legs")))
        whenever(repository.getExercises(null, 20, 0)).thenReturn(
            ExercisePage(items = listOf(exercise), total = 1, hasMore = false)
        )

        viewModel.start()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf(ExerciseCategory(9L, "Legs")), state.categories)
        assertEquals(listOf(exercise), state.exercises)
        assertTrue(state.endReached)
        assertFalse(state.isLoading)
        assertFalse(state.isError)
    }

    @Test
    fun `start failure sets error state`() = runTest {
        doThrow(RuntimeException()).whenever(repository).getCategories()

        viewModel.start()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isError)
        assertFalse(state.isLoading)
        assertTrue(state.exercises.isEmpty())
    }

    @Test
    fun `exercise clicked emits create habit effect`() = runTest {
        val effects = mutableListOf<ExerciseCatalogEffect>()
        val collector = launch { viewModel.effects.collect { effects.add(it) } }

        viewModel.dispatch(ExerciseCatalogIntent.ExerciseClicked(exercise))
        advanceUntilIdle()
        collector.cancel()

        assertEquals<List<ExerciseCatalogEffect>>(
            listOf(ExerciseCatalogEffect.CreateHabitRequested(exercise)),
            effects,
        )
    }
}
