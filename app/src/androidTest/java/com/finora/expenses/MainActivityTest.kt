package com.finora.expenses

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun appLaunchesSuccessfully() {
        composeTestRule.onNodeWithText("Expenses").assertExists()
    }
    
    @Test
    fun navigationBarIsDisplayed() {
        composeTestRule.onNodeWithText("Expenses").assertExists()
        composeTestRule.onNodeWithText("Reports").assertExists()
    }
    
    @Test
    fun canNavigateBetweenScreens() {
        composeTestRule.onNodeWithText("Reports").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Reports").assertExists()
        
        composeTestRule.onNodeWithText("Expenses").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Expenses").assertExists()
    }
    
    @Test
    fun fabIsDisplayedOnExpensesScreen() {
        composeTestRule.onNodeWithContentDescription("Add Expense").assertExists()
    }
}
