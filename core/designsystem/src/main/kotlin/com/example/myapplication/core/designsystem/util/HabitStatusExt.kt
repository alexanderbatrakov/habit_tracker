package com.example.myapplication.core.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.core.designsystem.R
import com.example.myapplication.domain.model.HabitStatus

@Composable
fun HabitStatus.displayName(): String = stringResource(
    when (this) {
        HabitStatus.CONTINUE -> R.string.status_active
        HabitStatus.PAUSED -> R.string.status_paused
        HabitStatus.ARCHIVED -> R.string.status_archived
    }
)
