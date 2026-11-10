package com.example.myapplication.feature.habitdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.designsystem.R as DsR
import com.example.myapplication.feature.habitdetail.R
import com.example.myapplication.core.designsystem.TestTags
import com.example.myapplication.feature.habitdetail.HabitDetailIntent
import com.example.myapplication.feature.habitdetail.HabitDetailState
import com.example.myapplication.core.designsystem.component.StatusSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    state: HabitDetailState,
    onIntent: (HabitDetailIntent) -> Unit,
    onBack: () -> Unit,
) {
    val habit = state.habit

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.name ?: stringResource(R.string.habit_detail_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(TestTags.HabitDetail.BACK_BUTTON),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(DsR.string.back_cd))
                    }
                },
            )
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

            habit != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.testTag(TestTags.HabitDetail.NAME),
                    )

                    Text(
                        text = habit.description.ifBlank { stringResource(R.string.habit_no_description) },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag(TestTags.HabitDetail.DESCRIPTION),
                    )

                    Text(
                        text = stringResource(DsR.string.habit_status_label),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.testTag(TestTags.HabitDetail.STATUS_LABEL),
                    )
                    StatusSelector(
                        selected = habit.status,
                        onSelect = { onIntent(HabitDetailIntent.StatusChanged(it)) },
                        tagForStatus = { TestTags.HabitDetail.statusChip(it) },
                        enabled = !state.isUpdating,
                    )

                    Text(
                        text = stringResource(DsR.string.streak_days, state.streak),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag(TestTags.HabitDetail.STREAK),
                    )

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = { onIntent(HabitDetailIntent.EditClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestTags.HabitDetail.EDIT_BUTTON),
                    ) {
                        Text(stringResource(R.string.edit_habit))
                    }

                    OutlinedButton(
                        onClick = { onIntent(HabitDetailIntent.DeleteClicked) },
                        enabled = !state.isDeleting,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestTags.HabitDetail.DELETE_BUTTON),
                    ) {
                        Text(stringResource(R.string.delete_habit))
                    }
                }
            }

            else -> Unit
        }
    }

    if (state.isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = { onIntent(HabitDetailIntent.DeleteDialogDismissed) },
            title = { Text(stringResource(R.string.delete_habit_confirm_title)) },
            text = { Text(stringResource(R.string.delete_habit_confirm_text, habit?.name.orEmpty())) },
            confirmButton = {
                TextButton(
                    onClick = { onIntent(HabitDetailIntent.DeleteConfirmed) },
                    modifier = Modifier.testTag(TestTags.HabitDetail.DELETE_CONFIRM_BUTTON),
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onIntent(HabitDetailIntent.DeleteDialogDismissed) },
                    modifier = Modifier.testTag(TestTags.HabitDetail.DELETE_CANCEL_BUTTON),
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}
