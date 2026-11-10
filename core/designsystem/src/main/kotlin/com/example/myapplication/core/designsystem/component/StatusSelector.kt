package com.example.myapplication.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.HabitStatus
import com.example.myapplication.core.designsystem.util.displayName

@Composable
fun StatusSelector(
    selected: HabitStatus,
    onSelect: (HabitStatus) -> Unit,
    tagForStatus: (HabitStatus) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HabitStatus.entries.forEach { status ->
            FilterChip(
                selected = status == selected,
                onClick = { onSelect(status) },
                enabled = enabled,
                label = { Text(status.displayName()) },
                modifier = Modifier
                    .selectable(
                        selected = status == selected,
                        role = Role.RadioButton,
                        onClick = { onSelect(status) },
                    )
                    .testTag(tagForStatus(status)),
            )
        }
    }
}
