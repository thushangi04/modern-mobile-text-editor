package com.ucsc.is2205.moderntexteditor

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class EditorScreenTest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<MainActivity>()

    /*
     * ---------------------------------------------------------
     * Main editor
     * ---------------------------------------------------------
     */

    @Test
    fun editorScreen_isDisplayed() {

        composeTestRule
            .onNodeWithText(
                "Modern Text Editor"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "New"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Save"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Save As"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Open"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Recent"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Settings"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "History"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Settings navigation
     * ---------------------------------------------------------
     */

    @Test
    fun settingsButton_opensSettingsScreen() {

        composeTestRule
            .onNodeWithText(
                "Settings"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Customize your editor experience."
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Back to Editor"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Settings back navigation
     * ---------------------------------------------------------
     */

    @Test
    fun settingsBack_returnsToEditor() {

        composeTestRule
            .onNodeWithText(
                "Settings"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Back to Editor"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Back to Editor"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Modern Text Editor"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "New"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Version History
     * ---------------------------------------------------------
     */

    @Test
    fun versionHistoryButton_opensVersionHistoryScreen() {

        composeTestRule
            .onNodeWithText(
                "History"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Version History"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Back"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Version History back
     * ---------------------------------------------------------
     */

    @Test
    fun versionHistoryBack_returnsToEditor() {

        composeTestRule
            .onNodeWithText(
                "History"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Back"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Modern Text Editor"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Markdown Preview
     * ---------------------------------------------------------
     */

    @Test
    fun previewButton_opensMarkdownPreview() {

        composeTestRule
            .onNodeWithText(
                "Preview"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Markdown Preview"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Back to Editor"
            )
            .assertIsDisplayed()
    }

    /*
     * ---------------------------------------------------------
     * Markdown Preview back
     * ---------------------------------------------------------
     */

    @Test
    fun markdownPreview_backReturnsToEditor() {

        composeTestRule
            .onNodeWithText(
                "Preview"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Markdown Preview"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Back to Editor"
            )
            .performClick()

        composeTestRule
            .waitForIdle()

        composeTestRule
            .onNodeWithText(
                "Modern Text Editor"
            )
            .assertIsDisplayed()
    }
}