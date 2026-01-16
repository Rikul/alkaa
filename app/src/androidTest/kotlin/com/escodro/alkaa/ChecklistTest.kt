package com.escodro.alkaa

import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test

class ChecklistTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Clean start - wait for list to load
        composeTestRule.waitForIdle()

        // Create testing task if it doesn't exist
        if (composeTestRule.onAllNodesWithText("Testing Task").fetchSemanticsNodes().isEmpty()) {
            createTask("Testing Task")
        }
    }

    @Test
    fun checklistSectionAndAddItemButtonExists() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Check if the checklist section exists
        composeTestRule.onNodeWithTag("checklist_section").assertExists()

        // Check if the add checklist item button exists
        composeTestRule.onNodeWithTag("checklist_add_item_text").assertExists()
    }

    @Test
    fun canAddChecklistItemWithCheckbox() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Add a checklist item
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("New Checklist Item")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        // Verify the checklist item was added
        composeTestRule.onNodeWithText("New Checklist Item").assertExists()

        // Verify the checkbox for the new item exists
        composeTestRule.onNodeWithTag("checkbox_New Checklist Item").assertExists()

        // Verify the delete button for the new item exists
        composeTestRule.onNodeWithTag("delete_button_New Checklist Item").assertExists()

        // Check the checkbox
        composeTestRule.onNodeWithTag("checkbox_New Checklist Item").performClick()

        // Verify the checkbox is checked
        composeTestRule.onNodeWithTag("checkbox_New Checklist Item").assertIsOn()

        // Clean up by deleting the added checklist item
        composeTestRule.onNodeWithTag("delete_button_New Checklist Item").performClick()
    }

    @Test
    fun checklistItemHasDeleteButtonAndDelete() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Add a checklist item
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item to Delete")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        // Verify the delete button for the added checklist item exists
        composeTestRule.onNodeWithTag("delete_button_Checklist Item to Delete").assertExists()

        // Click the delete button
        composeTestRule.onNodeWithTag("delete_button_Checklist Item to Delete").performClick()

        composeTestRule.waitForIdle()

        // Verify the checklist item was deleted
        composeTestRule.onNodeWithText("Checklist Item to Delete").assertDoesNotExist()
    }


    @Test
    fun checklistItemTextIsEditable() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Add a checklist item
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Editable Checklist Item")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        // Verify the checklist item was added
        composeTestRule.onNodeWithText("Editable Checklist Item").assertExists()

        // Edit the checklist item text
        composeTestRule.onNodeWithTag("item_text_Editable Checklist Item").performTextReplacement("Edited Checklist Item")

        // Verify the checklist item text was updated
        composeTestRule.onNodeWithText("Edited Checklist Item").assertExists()

        // Verify the old checklist item text no longer exists
        composeTestRule.onNodeWithText("Editable Checklist Item").assertDoesNotExist()

        // Delete the edited checklist item
        composeTestRule.onNodeWithTag("delete_button_Editable Checklist Item").performClick()
    }


    @Test
    fun canAddMultipleChecklistItems() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Add multiple checklist items
        val checklistItems = listOf("Checklist Item 1", "Checklist Item 2", "Checklist Item 3")
        checklistItems.forEach { item ->
            composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput(item)
            composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()
        }

        composeTestRule.waitForIdle()
        
        // Verify all checklist items were added
        checklistItems.forEach { item ->
            composeTestRule.onNodeWithText(item).assertExists()
        }

        composeTestRule.waitForIdle()
        
        // Clean up by deleting the added checklist items
        checklistItems.forEach { item ->
            composeTestRule.onNodeWithTag("delete_button_$item").performClick()
        }

        composeTestRule.waitForIdle()

        // Verify all checklist items were deleted
        checklistItems.forEach { item ->
            composeTestRule.onNodeWithText(item).assertDoesNotExist()
        }   

    }

    @Test
    fun addChecklistItemWithEmptyTextDoesNotAdd() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Try to add a checklist item with empty text
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        // verify that no delete button with empty text was added, which indicates that the item was not added
        composeTestRule.onNodeWithTag("delete_button_").assertDoesNotExist()
    }

    @Test
    fun addMultipeTasksWithChecklistItems() {

        createTask("Another Testing Task")
    
        // Add some checklist items to the Testing Task
        
        composeTestRule.onNodeWithText("Testing Task").performClick()
        composeTestRule.waitForIdle()

        // Add checklist items to the Testing Task
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item 1 for Testing Task")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item 2 for Testing Task")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()
        composeTestRule.onNodeWithTag("checkbox_Checklist Item 1 for Testing Task").performClick()
        
        // Go back to the task list and open the Another Testing Task details
        composeTestRule.onNodeWithContentDescription("Back").performClick()    
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Another Testing Task").performClick()

        // Add checklist items to the Another Testing Task
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item 1 for Another Testing Task")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item 2 for Another Testing Task")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()
        composeTestRule.onNodeWithTag("checkbox_Checklist Item 1 for Another Testing Task").performClick()

        // Go back to Testing Task details and verify the checklist items are correct
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Testing Task").performClick()
        composeTestRule.onNodeWithText("Checklist Item 1 for Testing Task").assertExists()
        composeTestRule.onNodeWithText("Checklist Item 2 for Testing Task").assertExists()
        composeTestRule.onNodeWithTag("checkbox_Checklist Item 1 for Testing Task").assertIsOn()

        // Go back to Another Testing Task details and verify the checklist items are correct
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Another Testing Task").performClick()
        composeTestRule.onNodeWithText("Checklist Item 1 for Another Testing Task").assertExists()
        composeTestRule.onNodeWithText("Checklist Item 2 for Another Testing Task").assertExists()
        composeTestRule.onNodeWithTag("checkbox_Checklist Item 1 for Another Testing Task").assertIsOn()

    }   

    @Test
    fun canDeleteChecklistItem() {

        // Go to the Task Details screen
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Add a checklist item
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item to Delete")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()
        
        composeTestRule.onNodeWithTag("checklist_add_item_text").performTextInput("Checklist Item to Keep")
        composeTestRule.onNodeWithTag("checklist_add_item_button").performClick()

        // Go back to the task list and open the Testing Task details again to ensure the checklist items are loaded
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // Click the delete button
        composeTestRule.onNodeWithTag("delete_button_Checklist Item to Delete").performClick()

        // Go back to the task list and open the Testing Task details again to ensure the checklist items are loaded
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Testing Task").performClick()

        // verify that the checklist item to keep still exists and the checklist item to delete does not exist
        composeTestRule.onNodeWithText("Checklist Item to Keep").assertExists()
        composeTestRule.onNodeWithText("Checklist Item to Delete").assertDoesNotExist()

        composeTestRule.waitForIdle()

    }


    // Helper to create a Task
    fun createTask(title: String) {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("add_task_fab").performClick()

        // Enter the task title
        composeTestRule.onNodeWithTag("add_task_title").performTextInput(title)

        // Save the task
        composeTestRule.onNodeWithTag("add_task_button").performClick()

        composeTestRule.waitForIdle()
    }
}
