package com.ucsc.is2205.moderntexteditor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucsc.is2205.moderntexteditor.data.repository.RepositoryProvider
import com.ucsc.is2205.moderntexteditor.editor.EditorScreen
import com.ucsc.is2205.moderntexteditor.settings.SettingsScreen
import com.ucsc.is2205.moderntexteditor.syntax.MarkdownPreviewScreen
import com.ucsc.is2205.moderntexteditor.version.VersionHistoryScreen
import com.ucsc.is2205.moderntexteditor.version.VersionViewModel
import com.ucsc.is2205.moderntexteditor.editor.EditorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

private const val EDITOR_ROUTE = "editor"
private const val SETTINGS_ROUTE = "settings"
private const val VERSION_HISTORY_ROUTE = "version_history/{fileName}"
private const val MARKDOWN_PREVIEW_ROUTE = "markdown_preview"

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()
        
    val context = LocalContext.current
    val repositoryProvider = remember {
        RepositoryProvider(context)
    }

    /*
     * Stores Markdown text while navigating
     * to the preview screen.
     */
    val markdownToPreview =
        remember {
            mutableStateOf("")
        }

    val editorViewModel: EditorViewModel = viewModel()

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
                editorViewModel = editorViewModel,
                
                onOpenSettings = {

                    navController.navigate(
                        SETTINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onOpenVersionHistory = { fileName ->
                    val encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8")
                    navController.navigate(
                        "version_history/$encodedFileName"
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
        ) { backStackEntry ->
            val encodedFileName = backStackEntry.arguments?.getString("fileName") ?: ""
            val fileName = java.net.URLDecoder.decode(encodedFileName, "UTF-8")
            
            val versionViewModel: VersionViewModel = viewModel(
                factory = VersionViewModel.provideFactory(
                    repositoryProvider.versionRepository
                )
            )

            VersionHistoryScreen(
                fileName = fileName,
                onBack = {
                    navController.popBackStack()
                },
                onRollback = { content ->
                    editorViewModel.updateText(content)
                    navController.popBackStack()
                },
                viewModel = versionViewModel
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