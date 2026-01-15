package com.escodro.alkaa

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class LaunchTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addTaskButtonExists() {
        composeTestRule.onNodeWithTag("add_task_fab").assertExists()
    }
}
