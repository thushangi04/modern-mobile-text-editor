package com.ucsc.is2205.moderntexteditor.editor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditorUiState(
    val fileName: String = "Untitled.txt",
    val text: String = "",
    val isModified: Boolean = false,
    val isReadOnly: Boolean = false,
    val wordWrapEnabled: Boolean = true,
    val encoding: String = "UTF-8",
    val canUndo: Boolean = false,
    val canRedo: Boolean = false
)

class EditorViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            EditorUiState()
        )

    val uiState: StateFlow<EditorUiState> =
        _uiState.asStateFlow()

    private var fileManager: FileManager? = null

    private var initialized = false

    private val undoStack =
        mutableListOf<String>()

    private val redoStack =
        mutableListOf<String>()

    private var recoveryJob: Job? = null

    companion object {
        private const val RECOVERY_DELAY_MS = 1500L
    }

    /*
     * ---------------------------------------------------------
     * Initialization
     * ---------------------------------------------------------
     */

    fun initialize(
        context: Context
    ) {

        if (initialized) {
            return
        }

        fileManager =
            FileManager(context.applicationContext)

        initialized = true

        loadRecoveryIfAvailable()
    }

    /*
     * ---------------------------------------------------------
     * Recovery
     * ---------------------------------------------------------
     */

    private fun loadRecoveryIfAvailable() {

        val manager =
            fileManager
                ?: return

        if (!manager.hasRecovery()) {
            return
        }

        val recoveredText =
            manager.loadRecovery()
                ?: return

        if (recoveredText.isEmpty()) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                fileName = "Recovered.txt",
                text = recoveredText,
                isModified = true
            )

        undoStack.clear()
        redoStack.clear()

        updateUndoRedoState()
    }

    private fun scheduleRecoverySave() {

        recoveryJob?.cancel()

        recoveryJob =
            viewModelScope.launch {

                delay(
                    RECOVERY_DELAY_MS
                )

                fileManager?.saveRecovery(
                    _uiState.value.text
                )
            }
    }

    fun clearRecovery() {

        recoveryJob?.cancel()

        fileManager?.clearRecovery()
    }

    fun hasRecovery(): Boolean {

        return fileManager?.hasRecovery() ?: false
    }

    fun saveRecovery(
        text: String
    ): Boolean {

        return fileManager?.saveRecovery(text) ?: false
    }

    fun loadRecovery(): String? {

        return fileManager?.loadRecovery()
    }

    /*
     * ---------------------------------------------------------
     * New file
     * ---------------------------------------------------------
     */

    fun createNewFile() {

        if (_uiState.value.text.isNotEmpty()) {

            undoStack.add(
                _uiState.value.text
            )
        }

        redoStack.clear()

        _uiState.value =
            EditorUiState(
                fileName = "Untitled.txt",
                text = "",
                isModified = false,
                isReadOnly = false,
                wordWrapEnabled =
                    _uiState.value.wordWrapEnabled,
                encoding = "UTF-8"
            )

        clearRecovery()

        updateUndoRedoState()
    }

    /*
     * ---------------------------------------------------------
     * Text editing
     * ---------------------------------------------------------
     */

    fun updateText(
        newText: String
    ) {

        val currentState =
            _uiState.value

        if (currentState.isReadOnly) {
            return
        }

        if (newText == currentState.text) {
            return
        }

        undoStack.add(
            currentState.text
        )

        if (undoStack.size > 100) {
            undoStack.removeAt(0)
        }

        redoStack.clear()

        _uiState.value =
            currentState.copy(
                text = newText,
                isModified = true
            )

        updateUndoRedoState()

        scheduleRecoverySave()
    }

    /*
     * ---------------------------------------------------------
     * Undo
     * ---------------------------------------------------------
     */

    fun undo() {

        if (undoStack.isEmpty()) {
            return
        }

        val currentText =
            _uiState.value.text

        val previousText =
            undoStack.removeAt(
                undoStack.lastIndex
            )

        redoStack.add(
            currentText
        )

        _uiState.value =
            _uiState.value.copy(
                text = previousText,
                isModified = true
            )

        updateUndoRedoState()

        scheduleRecoverySave()
    }

    /*
     * ---------------------------------------------------------
     * Redo
     * ---------------------------------------------------------
     */

    fun redo() {

        if (redoStack.isEmpty()) {
            return
        }

        val currentText =
            _uiState.value.text

        val nextText =
            redoStack.removeAt(
                redoStack.lastIndex
            )

        undoStack.add(
            currentText
        )

        _uiState.value =
            _uiState.value.copy(
                text = nextText,
                isModified = true
            )

        updateUndoRedoState()

        scheduleRecoverySave()
    }

    /*
     * ---------------------------------------------------------
     * Save
     * ---------------------------------------------------------
     */

    fun saveFile(): Boolean {

        val manager =
            fileManager
                ?: return false

        val state =
            _uiState.value

        val success =
            manager.saveFile(
                fileName = state.fileName,
                content = state.text
            )

        if (success) {

            _uiState.value =
                state.copy(
                    isModified = false
                )

            clearRecovery()
        }

        return success
    }

    /*
     * ---------------------------------------------------------
     * Save As
     * ---------------------------------------------------------
     */

    fun saveAs(
        newFileName: String
    ): Boolean {

        val manager =
            fileManager
                ?: return false

        val cleanName =
            newFileName.trim()

        if (cleanName.isBlank()) {
            return false
        }

        val success =
            manager.saveAs(
                fileName = cleanName,
                content = _uiState.value.text
            )

        if (success) {

            _uiState.value =
                _uiState.value.copy(
                    fileName = cleanName,
                    isModified = false
                )

            clearRecovery()
        }

        return success
    }

    /*
     * ---------------------------------------------------------
     * Open file
     * ---------------------------------------------------------
     */

    fun openFile(
        fileName: String
    ): Boolean {

        val manager =
            fileManager
                ?: return false

        val content =
            manager.openFile(
                fileName
            )
                ?: return false

        undoStack.clear()
        redoStack.clear()

        _uiState.value =
            _uiState.value.copy(
                fileName = fileName,
                text = content,
                isModified = false
            )

        clearRecovery()

        updateUndoRedoState()

        return true
    }

    /*
     * ---------------------------------------------------------
     * File lists
     * ---------------------------------------------------------
     */

    fun getSavedFiles(): List<String> {

        return fileManager
            ?.getSavedFiles()
            ?: emptyList()
    }

    fun getRecentFiles(): List<String> {

        return fileManager
            ?.getRecentFiles()
            ?: emptyList()
    }

    /*
     * ---------------------------------------------------------
     * Read-only mode
     * ---------------------------------------------------------
     */

    fun toggleReadOnly() {

        _uiState.value =
            _uiState.value.copy(
                isReadOnly =
                    !_uiState.value.isReadOnly
            )
    }

    /*
     * ---------------------------------------------------------
     * Word wrap
     * ---------------------------------------------------------
     */

    fun toggleWordWrap() {

        _uiState.value =
            _uiState.value.copy(
                wordWrapEnabled =
                    !_uiState.value.wordWrapEnabled
            )
    }

    /*
     * ---------------------------------------------------------
     * Undo / redo state
     * ---------------------------------------------------------
     */

    private fun updateUndoRedoState() {

        _uiState.value =
            _uiState.value.copy(
                canUndo =
                    undoStack.isNotEmpty(),

                canRedo =
                    redoStack.isNotEmpty()
            )
    }

    /*
     * ---------------------------------------------------------
     * Lifecycle
     * ---------------------------------------------------------
     */

    override fun onCleared() {

        recoveryJob?.cancel()

        super.onCleared()
    }
}