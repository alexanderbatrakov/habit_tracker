package com.example.myapplication.feature.edithabit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.designsystem.R as DsR
import com.example.myapplication.feature.edithabit.R
import com.example.myapplication.core.designsystem.TestTags
import com.example.myapplication.feature.edithabit.EditHabitIntent
import com.example.myapplication.feature.edithabit.EditHabitState
import com.example.myapplication.core.designsystem.component.StatusSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    state: EditHabitState,
    onIntent: (EditHabitIntent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_habit_title), modifier = Modifier.testTag(TestTags.EditHabit.TITLE)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag(TestTags.EditHabit.BACK_BUTTON)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(DsR.string.back_cd))
                    }
                },
            )
        },
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onIntent(EditHabitIntent.NameChanged(it)) },
                label = { Text(stringResource(DsR.string.habit_name_label)) },
                isError = state.nameErrorRes != null,
                supportingText = state.nameErrorRes?.let { res -> { Text(stringResource(res)) } },
                singleLine = true,
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.EditHabit.NAME_INPUT),
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { onIntent(EditHabitIntent.DescriptionChanged(it)) },
                label = { Text(stringResource(DsR.string.habit_description_label)) },
                enabled = !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp)
                    .testTag(TestTags.EditHabit.DESCRIPTION_INPUT),
            )

            Text(
                stringResource(DsR.string.habit_status_label),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.testTag(TestTags.EditHabit.STATUS_LABEL),
            )
            StatusSelector(
                selected = state.status,
                onSelect = { onIntent(EditHabitIntent.StatusChanged(it)) },
                tagForStatus = { TestTags.EditHabit.statusChip(it) },
                enabled = !state.isSaving,
            )

            Spacer(Modifier.padding(top = 8.dp))

            Button(
                onClick = { onIntent(EditHabitIntent.Save) },
                enabled = state.canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.EditHabit.SAVE_BUTTON),
            ) {
                Text(if (state.isSaving) stringResource(DsR.string.saving) else stringResource(DsR.string.save))
            }
        }
    }
}
