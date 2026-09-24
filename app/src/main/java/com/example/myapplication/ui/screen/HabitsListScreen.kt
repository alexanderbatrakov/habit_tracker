package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.TestTags
import com.example.myapplication.domain.model.HabitWithProgress
import com.example.myapplication.feature.habitslist.HabitsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsListScreen(
    state: HabitsListState,
    onCreateHabitClick: () -> Unit,
    onToggleDone: (Long) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.habits_list_title)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateHabitClick,
                modifier = Modifier.testTag(TestTags.HabitsList.ADD_HABIT_BUTTON),
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_habit_cd))
            }
        },
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.habits.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(stringResource(R.string.habits_list_empty))
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(state.habits, key = { it.habit.id }) { item ->
                        HabitRow(item, onToggleDone = onToggleDone)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitRow(
    item: HabitWithProgress,
    onToggleDone: (Long) -> Unit,
) {
    ListItem(
        headlineContent = { Text(item.habit.name) },
        supportingContent = {
            Column {
                if (item.habit.description.isNotBlank()) {
                    Text(item.habit.description)
                }
                Text(
                    text = stringResource(R.string.streak_days, item.streak),
                    style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                )
            }
        },
        trailingContent = {
            Switch(
                checked = item.isDoneToday,
                onCheckedChange = { onToggleDone(item.habit.id) },
            )
        },
    )
}
