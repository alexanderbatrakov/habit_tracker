package com.example.myapplication.page_object

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.myapplication.TestTags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class HabitCreationPageObject(provider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<HabitCreationPageObject>(provider) {
    val screenTitle: KNode = child { hasTestTag(TestTags.CreateHabit.TITLE) }
    val backButton: KNode = child { hasTestTag(TestTags.CreateHabit.BACK_BUTTON) }
    val nameInput: KNode = child { hasTestTag(TestTags.CreateHabit.NAME_INPUT) }
    val descriptionInput: KNode = child { hasTestTag(TestTags.CreateHabit.DESCRIPTION_INPUT) }
    val statusLabel: KNode = child { hasTestTag(TestTags.CreateHabit.STATUS_LABEL) }
    val saveButton: KNode = child { hasTestTag(TestTags.CreateHabit.SAVE_BUTTON) }

    fun assertEmptyCreationHabitIsDisplayed() {
        screenTitle.assertIsDisplayed()
        backButton.assertIsDisplayed()
        nameInput.assertIsDisplayed()
        descriptionInput.assertIsDisplayed()
        statusLabel.assertIsDisplayed()
        saveButton.assertIsDisplayed()
    }
}