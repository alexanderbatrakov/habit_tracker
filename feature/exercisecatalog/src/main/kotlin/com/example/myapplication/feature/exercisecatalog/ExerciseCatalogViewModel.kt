package com.example.myapplication.feature.exercisecatalog

import com.example.myapplication.domain.model.ExercisePage
import com.example.myapplication.domain.repository.WgerRepository
import com.example.myapplication.core.common.ReduceResult
import com.example.myapplication.core.common.ReducerStore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ExerciseCatalogViewModel @Inject constructor(
    private val wgerRepository: WgerRepository,
) : ReducerStore<ExerciseCatalogState, ExerciseCatalogIntent, ExerciseCatalogEffect>(ExerciseCatalogState()) {

    private var started = false

    fun start() {
        if (started) return
        started = true
        dispatch(ExerciseCatalogIntent.InitialLoadRequested)
    }

    override fun reduce(
        state: ExerciseCatalogState,
        intent: ExerciseCatalogIntent,
    ): ReduceResult<ExerciseCatalogState, ExerciseCatalogEffect> =
        ExerciseCatalogReducer.reduce(state, intent)

    override suspend fun handleEffect(effect: ExerciseCatalogEffect) {
        when (effect) {
            is ExerciseCatalogEffect.FetchFirstPage -> {
                val result = runCatching {
                    coroutineScope {
                        val categories = async { wgerRepository.getCategories() }
                        val page = async {
                            wgerRepository.getExercises(effect.categoryId, PAGE_SIZE, offset = 0)
                        }
                        ExerciseCatalogIntent.InitialLoaded(
                            categories = categories.await(),
                            exercises = page.await().items,
                            total = page.await().total,
                            hasMore = page.await().hasMore,
                        )
                    }
                }
                result.fold(
                    onSuccess = { dispatch(it) },
                    onFailure = { dispatch(ExerciseCatalogIntent.InitialLoadFailed) },
                )
            }

            is ExerciseCatalogEffect.FetchNextPage -> {
                val result = runCatching {
                    wgerRepository.getExercises(effect.categoryId, PAGE_SIZE, effect.offset)
                }
                result.fold(
                    onSuccess = { page: ExercisePage ->
                        dispatch(
                            ExerciseCatalogIntent.NextPageLoaded(
                                exercises = page.items,
                                total = page.total,
                                hasMore = page.hasMore,
                            )
                        )
                    },
                    onFailure = { dispatch(ExerciseCatalogIntent.NextPageFailed) },
                )
            }

            is ExerciseCatalogEffect.CreateHabitRequested -> emitEffect(effect)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
