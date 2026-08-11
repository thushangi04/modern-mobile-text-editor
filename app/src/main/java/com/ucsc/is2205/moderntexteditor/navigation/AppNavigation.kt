package com.ucsc.is2205.moderntexteditor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucsc.is2205.moderntexteditor.editor.EditorScreen
import com.ucsc.is2205.moderntexteditor.settings.SettingsScreen
import com.ucsc.is2205.moderntexteditor.syntax.MarkdownPreviewScreen
import com.ucsc.is2205.moderntexteditor.version.VersionHistoryScreen

private const val EDITOR_ROUTE = "editor"
private const val SETTINGS_ROUTE = "settings"
private const val VERSION_HISTORY_ROUTE = "version_history"
private const val MARKDOWN_PREVIEW_ROUTE = "markdown_preview"

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    /*
     * Stores Markdown text while navigating
     * to the preview screen.
     */
    val markdownToPreview =
        remember {
            mutableStateOf("")
        }

    NavHost(
        navController = navController,
        startDestination = EDITOR_ROUTE
    ) {

        /*
         * -----------------------------------------------------
         * Editor
         * -----------------------------------------------------
         */

        composable(
            route = EDITOR_ROUTE
        ) {

            EditorScreen(

                onOpenSettings = {

                    navController.navigate(
                        SETTINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onOpenVersionHistory = {

                    navController.navigate(
                        VERSION_HISTORY_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onPreviewMarkdown = { markdown ->

                    markdownToPreview.value =
                        markdown

                    navController.navigate(
                        MARKDOWN_PREVIEW_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * -----------------------------------------------------
         * Settings
         * -----------------------------------------------------
         */

        composable(
            route = SETTINGS_ROUTE
        ) {

            SettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        /*
         * -----------------------------------------------------
         * Version History
         * -----------------------------------------------------
         */

        composable(
            route = VERSION_HISTORY_ROUTE
        ) {

            VersionHistoryScreen(

                fileName = "",

                onBack = {

                    navController.popBackStack()
                }
            )
        }

        /*
         * -----------------------------------------------------
         * Markdown Preview
         * -----------------------------------------------------
         */

        composable(
            route = MARKDOWN_PREVIEW_ROUTE
        ) {

            MarkdownPreviewScreen(

                markdown =
                    markdownToPreview.value,

                onBack = {

                    navController.popBackStack()
                }
            )
        }
    }
}