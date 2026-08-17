package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.entity.HabitStatus
import com.example.myapplication.feature.createhabit.CreateHabitIntent
import com.example.myapplication.feature.createhabit.CreateHabitState
import com.example.myapplication.ui.util.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    state: CreateHabitState,
    onIntent: (CreateHabitIntent) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новая привычка") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onIntent(CreateHabitIntent.NameChanged(it)) },
                label = { Text("Название") },
                isError = state.nameError != null,
                supportingText = state.nameError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { onIntent(CreateHabitIntent.DescriptionChanged(it)) },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp),
            )

            Text("Статус", style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
            StatusSelector(
                selected = state.status,
                onSelect = { onIntent(CreateHabitIntent.StatusChanged(it)) },
            )

            Spacer(Modifier.padding(top = 8.dp))

            Button(
                onClick = { onIntent(CreateHabitIntent.Save) },
                enabled = state.canSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (state.isSaving) "Сохранение…" else "Сохранить")
            }
        }
    }
}

@Composable
private fun StatusSelector(
    selected: HabitStatus,
    onSelect: (HabitStatus) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HabitStatus.entries.forEach { status ->
            FilterChip(
                selected = status == selected,
                onClick = { onSelect(status) },
                label = { Text(status.displayName()) },
                modifier = Modifier.selectable(
                    selected = status == selected,
                    role = Role.RadioButton,
                    onClick = { onSelect(status) },
                ),
            )
        }
    }
}
