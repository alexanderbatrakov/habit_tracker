package com.example.myapplication.feature.exercisecatalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.designsystem.R as DsR
import com.example.myapplication.feature.exercisecatalog.R
import com.example.myapplication.core.designsystem.TestTags
import com.example.myapplication.domain.model.Exercise
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogIntent
import com.example.myapplication.feature.exercisecatalog.ExerciseCatalogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCatalogScreen(
    state: ExerciseCatalogState,
    onIntent: (ExerciseCatalogIntent) -> Unit,
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember(state.exercises.size) {
        derivedStateOf {
            val info = listState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - 2
        }
    }
    LaunchedEffect(shouldLoadMore, state.exercises.size) {
        if (shouldLoadMore) {
            onIntent(ExerciseCatalogIntent.LoadMoreRequested)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.exercise_catalog_title),
                        modifier = Modifier.testTag(TestTags.ExerciseCatalog.TITLE),
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = state.selectedCategoryId == null,
                    onClick = { onIntent(ExerciseCatalogIntent.CategorySelected(null)) },
                    label = { Text(stringResource(R.string.exercise_catalog_all)) },
                    modifier = Modifier.testTag(TestTags.ExerciseCatalog.categoryChip(null)),
                )
                state.categories.forEach { category ->
                    FilterChip(
                        selected = state.selectedCategoryId == category.id,
                        onClick = { onIntent(ExerciseCatalogIntent.CategorySelected(category.id)) },
                        label = { Text(category.name) },
                        modifier = Modifier.testTag(TestTags.ExerciseCatalog.categoryChip(category.id)),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                when {
                    state.isError && state.exercises.isEmpty() -> ErrorState(
                        onRetry = { onIntent(ExerciseCatalogIntent.RetryClicked) },
                    )

                    state.isLoading && state.exercises.isEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }

                    state.exercises.isEmpty() -> Text(
                        text = stringResource(R.string.exercise_catalog_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    )

                    else -> LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(TestTags.ExerciseCatalog.LIST),
                    ) {
                        items(state.exercises, key = { it.id }) { exercise ->
                            ExerciseRow(
                                exercise = exercise,
                                onClick = { onIntent(ExerciseCatalogIntent.ExerciseClicked(exercise)) },
                            )
                            HorizontalDivider()
                        }
                        item(key = "footer") {
                            CatalogFooter(state = state, onIntent = onIntent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseRow(
    exercise: Exercise,
    onClick: () -> Unit,
) {
    val details = listOf(exercise.categoryName)
        .plus(exercise.muscles.take(2))
        .plus(exercise.equipment)
        .filter { it.isNotBlank() }
        .joinToString(" · ")

    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(TestTags.ExerciseCatalog.exerciseItem(exercise.id)),
        headlineContent = { Text(exercise.name) },
        supportingContent = details.takeIf { it.isNotBlank() }?.let { { Text(it, maxLines = 1) } },
        trailingContent = {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.create_habit_from_exercise_cd),
                )
            }
        },
    )
}

@Composable
private fun CatalogFooter(
    state: ExerciseCatalogState,
    onIntent: (ExerciseCatalogIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            state.isLoadingMore -> CircularProgressIndicator()

            state.isLoadMoreError -> TextButton(
                onClick = { onIntent(ExerciseCatalogIntent.RetryClicked) },
            ) {
                Text(stringResource(R.string.exercise_catalog_load_more_error))
            }

            state.endReached -> Text(
                text = stringResource(R.string.exercise_catalog_end, state.exercises.size),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.exercise_catalog_error),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.testTag(TestTags.ExerciseCatalog.RETRY_BUTTON),
        ) {
            Text(stringResource(DsR.string.retry))
        }
    }
}
