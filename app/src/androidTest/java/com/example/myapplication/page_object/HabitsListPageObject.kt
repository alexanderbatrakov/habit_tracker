package com.example.myapplication.page_object

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.myapplication.R
import com.example.myapplication.TestTags
import com.example.myapplication.util.asString
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class HabitsListPageObject(provider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<HabitsListPageObject>(provider) {
    val emptyHabitsText: KNode = child { hasText(R.string.habits_list_empty.asString()) }
    val habitCreationButton: KNode = child { hasTestTag(TestTags.HabitsList.ADD_HABIT_BUTTON) }

    fun assertEmptyTextIsDisplayed() {
        emptyHabitsText.assertIsDisplayed()
    }

    fun tapHabitCreationButton() {
        habitCreationButton.assertIsDisplayed()
        habitCreationButton.performClick()
    }
}