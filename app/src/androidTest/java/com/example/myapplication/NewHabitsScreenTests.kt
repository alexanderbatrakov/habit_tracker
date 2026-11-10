package com.example.myapplication

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.myapplication.page_object.HabitCreationPageObject
import com.example.myapplication.page_object.HabitsListPageObject
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class NewHabitsScreenTests : TestCase(Kaspresso.Builder.withComposeSupport()) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun emptyNewHabitsScreenIsDisplayed() = run {
        step("Отображение пустого экрана 'Новая привычка'") {
            HabitsListPageObject(composeTestRule)
                .tapHabitCreationButton()
            HabitCreationPageObject(composeTestRule)
                .assertEmptyCreationHabitIsDisplayed()
        }
    }
}
