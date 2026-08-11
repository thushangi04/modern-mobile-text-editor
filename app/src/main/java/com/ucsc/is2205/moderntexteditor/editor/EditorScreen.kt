package com.ucsc.is2205.moderntexteditor.editor

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ucsc.is2205.moderntexteditor.editor.components.EditorStatusBar
import com.ucsc.is2205.moderntexteditor.editor.components.EditorTextArea
import com.ucsc.is2205.moderntexteditor.editor.components.EditorToolbar
import com.ucsc.is2205.moderntexteditor.editor.components.EditorTopBar
import com.ucsc.is2205.moderntexteditor.editor.components.FileSelectionPanel
import com.ucsc.is2205.moderntexteditor.editor.components.SaveAsDialog
import com.ucsc.is2205.moderntexteditor.editor.components.SearchReplaceDialog
import kotlinx.coroutines.delay

@Composable
fun EditorScreen(
    onOpenSettings: () -> Unit = {},
    onOpenVersionHistory: () -> Unit = {},
    onPreviewMarkdown: (String) -> Unit = {},
    editorViewModel: EditorViewModel = viewModel()
) {

    val context = LocalContext.current
    val uiState by editorViewModel.uiState.collectAsState()

    var savedFiles by remember {
        mutableStateOf(emptyList<String>())
    }

    var recentFiles by remember {
        mutableStateOf(emptyList<String>())
    }

    var showOpenFiles by remember {
        mutableStateOf(false)
    }

    var showRecentFiles by remember {
        mutableStateOf(false)
    }

    var showSaveAsDialog by remember {
        mutableStateOf(false)
    }

    var showSearchDialog by remember {
        mutableStateOf(false)
    }

    var showRecoveryDialog by remember {
        mutableStateOf(false)
    }

    /*
     * ---------------------------------------------------------
     * Initialization
     * ---------------------------------------------------------
     */

    LaunchedEffect(Unit) {

        editorViewModel.initialize(context)

        savedFiles =
            editorViewModel.getSavedFiles()

        recentFiles =
            editorViewModel.getRecentFiles()

        if (editorViewModel.hasRecovery()) {
            showRecoveryDialog = true
        }
    }

    /*
     * ---------------------------------------------------------
     * Auto-save recovery
     * ---------------------------------------------------------
     */

    LaunchedEffect(
        uiState.text,
        uiState.isModified
    ) {

        if (!uiState.isModified) {
            return@LaunchedEffect
        }

        delay(5000)

        editorViewModel.saveRecovery(
            uiState.text
        )
    }

    /*
     * ---------------------------------------------------------
     * Main screen
     * ---------------------------------------------------------
     */

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                )
        ) {

            /*
             * -------------------------------------------------
             * Application header
             * -------------------------------------------------
             */

            AppHeader(
                onOpenSettings = onOpenSettings,
                onOpenVersionHistory = onOpenVersionHistory
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * -------------------------------------------------
             * File information + file operations
             * -------------------------------------------------
             */

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                EditorTopBar(
                    uiState = uiState,

                    onNewFile = {
                        editorViewModel.createNewFile()

                        showOpenFiles = false
                        showRecentFiles = false
                    },

                    onSaveFile = {

                        val saved =
                            editorViewModel.saveFile()

                        if (saved) {

                            editorViewModel.clearRecovery()

                            savedFiles =
                                editorViewModel.getSavedFiles()

                            recentFiles =
                                editorViewModel.getRecentFiles()

                            Toast.makeText(
                                context,
                                "File saved",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                context,
                                "Could not save file",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },

                    onSaveAs = {
                        showSaveAsDialog = true
                    },

                    onOpenFile = {

                        savedFiles =
                            editorViewModel.getSavedFiles()

                        showRecentFiles = false
                        showOpenFiles = true
                    },

                    onRecentFiles = {

                        recentFiles =
                            editorViewModel.getRecentFiles()

                        showOpenFiles = false
                        showRecentFiles = true
                    },

                    onToggleReadOnly = {
                        editorViewModel.toggleReadOnly()
                    }
                )
            }

            /*
             * -------------------------------------------------
             * File selection panel
             * -------------------------------------------------
             */

            if (showOpenFiles) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                FileSelectionPanel(
                    title = "Open File",
                    files = savedFiles,

                    onFileSelected = { fileName ->

                        val opened =
                            editorViewModel.openFile(
                                fileName
                            )

                        if (opened) {

                            Toast.makeText(
                                context,
                                "File opened",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                context,
                                "Could not open file",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        showOpenFiles = false
                    },

                    onClose = {
                        showOpenFiles = false
                    }
                )
            }

            if (showRecentFiles) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                FileSelectionPanel(
                    title = "Recent Files",
                    files = recentFiles,

                    onFileSelected = { fileName ->

                        val opened =
                            editorViewModel.openFile(
                                fileName
                            )

                        if (opened) {

                            Toast.makeText(
                                context,
                                "Recent file opened",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                context,
                                "Could not open file",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        showRecentFiles = false
                    },

                    onClose = {
                        showRecentFiles = false
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * -------------------------------------------------
             * Editor toolbar
             * -------------------------------------------------
             */

            EditorToolbar(
                uiState = uiState,

                onUndo =
                    editorViewModel::undo,

                onRedo =
                    editorViewModel::redo,

                onSearch = {
                    showSearchDialog = true
                },

                onToggleWordWrap =
                    editorViewModel::toggleWordWrap,

                onPreview = {
                    onPreviewMarkdown(
                        uiState.text
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * -------------------------------------------------
             * Workspace
             * -------------------------------------------------
             */

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    /*
                     * Workspace header
                     */

                    WorkspaceHeader(
                        fileName = uiState.fileName,
                        isReadOnly = uiState.isReadOnly,
                        isModified = uiState.isModified
                    )

                    HorizontalDivider(
                        color =
                            MaterialTheme.colorScheme.outlineVariant
                    )

                    /*
                     * Main text editor
                     */

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(6.dp)
                    ) {

                        EditorTextArea(
                            text = uiState.text,

                            fileName =
                                uiState.fileName,

                            isReadOnly =
                                uiState.isReadOnly,

                            wordWrapEnabled =
                                uiState.wordWrapEnabled,

                            onTextChanged =
                                editorViewModel::updateText,

                            modifier =
                                Modifier.fillMaxSize()
                        )
                    }

                    HorizontalDivider(
                        color =
                            MaterialTheme.colorScheme.outlineVariant
                    )

                    /*
                     * Status bar
                     */

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            )
                    ) {

                        EditorStatusBar(
                            uiState = uiState
                        )
                    }
                }
            }
        }
    }

    /*
     * ---------------------------------------------------------
     * Recovery dialog
     * ---------------------------------------------------------
     */

    if (showRecoveryDialog) {

        RecoveryDialog(

            onRestore = {

                val recoveryText =
                    editorViewModel.loadRecovery()

                if (recoveryText != null) {

                    editorViewModel.updateText(
                        recoveryText
                    )

                    editorViewModel.clearRecovery()

                    Toast.makeText(
                        context,
                        "Recovery restored",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        context,
                        "Could not restore recovery data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                showRecoveryDialog = false
            },

            onDiscard = {

                editorViewModel.clearRecovery()

                Toast.makeText(
                    context,
                    "Recovery discarded",
                    Toast.LENGTH_SHORT
                ).show()

                showRecoveryDialog = false
            }
        )
    }

    /*
     * ---------------------------------------------------------
     * Save As dialog
     * ---------------------------------------------------------
     */

    if (showSaveAsDialog) {

        SaveAsDialog(
            currentFileName =
                uiState.fileName,

            onSave = { newFileName ->

                val success =
                    editorViewModel.saveAs(
                        newFileName
                    )

                if (success) {

                    editorViewModel.clearRecovery()

                    savedFiles =
                        editorViewModel.getSavedFiles()

                    recentFiles =
                        editorViewModel.getRecentFiles()

                    Toast.makeText(
                        context,
                        "File saved as $newFileName",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        context,
                        "Could not save file",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                showSaveAsDialog = false
            },

            onCancel = {
                showSaveAsDialog = false
            }
        )
    }

    /*
     * ---------------------------------------------------------
     * Search & Replace dialog
     * ---------------------------------------------------------
     */

    if (showSearchDialog) {

        SearchReplaceDialog(
            initialText = uiState.text,

            onReplace = {
                    searchText,
                    replacementText,
                    caseSensitive ->

                val result =
                    replaceFirstMatch(
                        text = uiState.text,
                        search = searchText,
                        replacement = replacementText,
                        caseSensitive = caseSensitive
                    )

                if (result != null) {

                    editorViewModel.updateText(
                        result
                    )

                    Toast.makeText(
                        context,
                        "Match replaced",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        context,
                        "Text not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            onReplaceAll = {
                    searchText,
                    replacementText,
                    caseSensitive ->

                if (searchText.isEmpty()) {
                    return@SearchReplaceDialog
                }

                val result =
                    replaceAllMatches(
                        text = uiState.text,
                        search = searchText,
                        replacement = replacementText,
                        caseSensitive = caseSensitive
                    )

                if (result.first != uiState.text) {

                    editorViewModel.updateText(
                        result.first
                    )
                }

                Toast.makeText(
                    context,
                    "${result.second} replacement(s)",
                    Toast.LENGTH_SHORT
                ).show()
            },

            onFindNext = {
                    searchText,
                    caseSensitive ->

                val found =
                    findMatch(
                        text = uiState.text,
                        search = searchText,
                        caseSensitive = caseSensitive,
                        startIndex = 0
                    )

                Toast.makeText(
                    context,
                    if (found >= 0) {
                        "Match found at character ${found + 1}"
                    } else {
                        "Text not found"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            },

            onFindPrevious = {
                    searchText,
                    caseSensitive ->

                val found =
                    findPreviousMatch(
                        text = uiState.text,
                        search = searchText,
                        caseSensitive = caseSensitive
                    )

                Toast.makeText(
                    context,
                    if (found >= 0) {
                        "Match found at character ${found + 1}"
                    } else {
                        "Text not found"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            },

            onClose = {
                showSearchDialog = false
            }
        )
    }
}

/*
 * -------------------------------------------------------------
 * Application header
 * -------------------------------------------------------------
 */

@Composable
private fun AppHeader(
    onOpenSettings: () -> Unit,
    onOpenVersionHistory: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 2.dp,
                vertical = 4.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Modern Text Editor",
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(1.dp)
            )

            Text(
                text = "Simple • Fast • Focused",
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }

        OutlinedButton(
            onClick = onOpenVersionHistory
        ) {

            Text(
                text = "History"
            )
        }

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        OutlinedButton(
            onClick = onOpenSettings
        ) {

            Text(
                text = "Settings"
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Workspace header
 * -------------------------------------------------------------
 */

@Composable
private fun WorkspaceHeader(
    fileName: String,
    isReadOnly: Boolean,
    isModified: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 14.dp,
                vertical = 9.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Editor",
                style =
                    MaterialTheme.typography.titleSmall,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = fileName,
                style =
                    MaterialTheme.typography.labelSmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                maxLines = 1
            )
        }

        Surface(
            color =
                when {
                    isReadOnly ->
                        MaterialTheme.colorScheme.errorContainer

                    isModified ->
                        MaterialTheme.colorScheme.primaryContainer

                    else ->
                        MaterialTheme.colorScheme.surfaceVariant
                },

            contentColor =
                when {
                    isReadOnly ->
                        MaterialTheme.colorScheme.onErrorContainer

                    isModified ->
                        MaterialTheme.colorScheme.onPrimaryContainer

                    else ->
                        MaterialTheme.colorScheme.onSurfaceVariant
                },

            shape =
                MaterialTheme.shapes.small
        ) {

            Text(
                text =
                    when {
                        isReadOnly ->
                            "READ ONLY"

                        isModified ->
                            "MODIFIED"

                        else ->
                            "READY"
                    },

                modifier =
                    Modifier.padding(
                        horizontal = 9.dp,
                        vertical = 4.dp
                    ),

                style =
                    MaterialTheme.typography.labelSmall,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

/*
 * -------------------------------------------------------------
 * Recovery dialog
 * -------------------------------------------------------------
 */

@Composable
private fun RecoveryDialog(
    onRestore: () -> Unit,
    onDiscard: () -> Unit
) {

    AlertDialog(
        onDismissRequest =
            onDiscard,

        title = {

            Text(
                text = "Recovery data found"
            )
        },

        text = {

            Text(
                text =
                    "The editor found unsaved recovery data " +
                            "from a previous session. " +
                            "Would you like to restore it?"
            )
        },

        confirmButton = {

            Button(
                onClick = onRestore
            ) {

                Text(
                    text = "Restore"
                )
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick = onDiscard
            ) {

                Text(
                    text = "Discard"
                )
            }
        }
    )
}

/*
 * -------------------------------------------------------------
 * Search helpers
 * -------------------------------------------------------------
 */

private fun findMatch(
    text: String,
    search: String,
    caseSensitive: Boolean,
    startIndex: Int
): Int {

    if (search.isEmpty()) {
        return -1
    }

    return if (caseSensitive) {

        text.indexOf(
            string = search,
            startIndex = startIndex
        )

    } else {

        text.indexOf(
            string = search,
            startIndex = startIndex,
            ignoreCase = true
        )
    }
}

private fun findPreviousMatch(
    text: String,
    search: String,
    caseSensitive: Boolean
): Int {

    if (search.isEmpty()) {
        return -1
    }

    var lastFound = -1
    var currentIndex = 0

    while (currentIndex < text.length) {

        val found =
            findMatch(
                text = text,
                search = search,
                caseSensitive = caseSensitive,
                startIndex = currentIndex
            )

        if (found < 0) {
            break
        }

        lastFound = found

        currentIndex =
            found + 1
    }

    return lastFound
}

private fun replaceFirstMatch(
    text: String,
    search: String,
    replacement: String,
    caseSensitive: Boolean
): String? {

    if (search.isEmpty()) {
        return null
    }

    val index =
        findMatch(
            text = text,
            search = search,
            caseSensitive = caseSensitive,
            startIndex = 0
        )

    if (index < 0) {
        return null
    }

    return buildString {

        append(
            text.substring(
                0,
                index
            )
        )

        append(
            replacement
        )

        append(
            text.substring(
                index + search.length
            )
        )
    }
}

private fun replaceAllMatches(
    text: String,
    search: String,
    replacement: String,
    caseSensitive: Boolean
): Pair<String, Int> {

    if (search.isEmpty()) {

        return Pair(
            text,
            0
        )
    }

    var currentIndex = 0
    var replacementCount = 0

    val result =
        StringBuilder()

    while (currentIndex < text.length) {

        val found =
            findMatch(
                text = text,
                search = search,
                caseSensitive = caseSensitive,
                startIndex = currentIndex
            )

        if (found < 0) {

            result.append(
                text.substring(
                    currentIndex
                )
            )

            break
        }

        result.append(
            text.substring(
                currentIndex,
                found
            )
        )

        result.append(
            replacement
        )

        replacementCount++

        currentIndex =
            found + search.length
    }

    return Pair(
        result.toString(),
        replacementCount
    )
}