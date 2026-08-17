package com.ucsc.is2205.moderntexteditor.editor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucsc.is2205.moderntexteditor.data.repository.RepositoryProvider
import com.ucsc.is2205.moderntexteditor.domain.model.EditorFile
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import com.ucsc.is2205.moderntexteditor.domain.repository.FileRepository
import com.ucsc.is2205.moderntexteditor.domain.repository.VersionRepository
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

    private var fileRepository: FileRepository? = null

    private var versionRepository: VersionRepository? = null

    private var initialized = false

    private val undoStack =
        mutableListOf<String>()

    private val redoStack =
        mutableListOf<String>()

    private var recoveryJob: Job? = null

    companion object {
        private const val RECOVERY_DELAY_MS = 1500L
    }

    fun initialize(
        context: Context
    ) {
        if (initialized) {
            return
        }

        fileManager =
            FileManager(
                context.applicationContext
            )

        val repositoryProvider =
            RepositoryProvider(
                context.applicationContext
            )

        fileRepository =
            repositoryProvider.fileRepository

        versionRepository =
            repositoryProvider.versionRepository

        initialized = true

        loadRecoveryIfAvailable()
    }

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
        return fileManager
            ?.hasRecovery()
            ?: false
    }

    fun saveRecovery(
        text: String
    ): Boolean {
        return fileManager
            ?.saveRecovery(text)
            ?: false
    }

    fun loadRecovery(): String? {
        return fileManager
            ?.loadRecovery()
    }

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

    fun restoreVersion(
        content: String
    ) {
        updateText(
            newText = content
        )
    }

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
            val savedState =
                state.copy(
                    isModified = false
                )

            _uiState.value =
                savedState

            saveFileMetadata(
                state = savedState,
                updateModifiedTime = true
            )

            saveVersionSnapshot(
                state = savedState
            )

            clearRecovery()
        }

        return success
    }

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
            val savedState =
                _uiState.value.copy(
                    fileName = cleanName,
                    isModified = false
                )

            _uiState.value =
                savedState

            saveFileMetadata(
                state = savedState,
                updateModifiedTime = true
            )

            saveVersionSnapshot(
                state = savedState
            )

            clearRecovery()
        }

        return success
    }

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
                isModified = false,
                isReadOnly = false
            )

        loadAndUpdateFileMetadata(
            fileName = fileName
        )

        clearRecovery()

        updateUndoRedoState()

        return true
    }

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

    fun toggleReadOnly() {
        val newReadOnlyStatus =
            !_uiState.value.isReadOnly

        _uiState.value =
            _uiState.value.copy(
                isReadOnly =
                    newReadOnlyStatus
            )

        persistReadOnlyStatus(
            isReadOnly = newReadOnlyStatus
        )
    }

    fun toggleWordWrap() {
        _uiState.value =
            _uiState.value.copy(
                wordWrapEnabled =
                    !_uiState.value.wordWrapEnabled
            )
    }

    private fun saveFileMetadata(
        state: EditorUiState,
        updateModifiedTime: Boolean
    ) {
        val repository =
            fileRepository
                ?: return

        val currentTime =
            System.currentTimeMillis()

        viewModelScope.launch {
            val existingFile =
                repository.getFile(
                    state.fileName
                )

            val metadata =
                EditorFile(
                    fileName = state.fileName,
                    encoding = state.encoding,
                    isReadOnly = state.isReadOnly,
                    lastOpenedAt = currentTime,
                    lastModifiedAt =
                        if (updateModifiedTime) {
                            currentTime
                        } else {
                            existingFile
                                ?.lastModifiedAt
                                ?: currentTime
                        }
                )

            repository.saveFileMetadata(
                metadata
            )
        }
    }

    private fun saveVersionSnapshot(
        state: EditorUiState
    ) {
        val repository =
            versionRepository
                ?: return

        viewModelScope.launch {
            try {
                repository.createVersion(
                    FileVersion(
                        fileName = state.fileName,
                        content = state.text,
                        timestamp =
                            System.currentTimeMillis()
                    )
                )
            } catch (_: Exception) {
                // The physical file remains saved if version storage fails.
            }
        }
    }

    private fun loadAndUpdateFileMetadata(
        fileName: String
    ) {
        val repository =
            fileRepository
                ?: return

        viewModelScope.launch {
            val currentTime =
                System.currentTimeMillis()

            val existingFile =
                repository.getFile(
                    fileName
                )

            val metadata =
                if (existingFile == null) {
                    EditorFile(
                        fileName = fileName,
                        encoding =
                            _uiState.value.encoding,
                        isReadOnly = false,
                        lastOpenedAt = currentTime,
                        lastModifiedAt = currentTime
                    )
                } else {
                    existingFile.copy(
                        lastOpenedAt = currentTime
                    )
                }

            repository.saveFileMetadata(
                metadata
            )

            if (_uiState.value.fileName == fileName) {
                _uiState.value =
                    _uiState.value.copy(
                        encoding = metadata.encoding,
                        isReadOnly =
                            metadata.isReadOnly
                    )
            }
        }
    }

    private fun persistReadOnlyStatus(
        isReadOnly: Boolean
    ) {
        val repository =
            fileRepository
                ?: return

        val manager =
            fileManager
                ?: return

        val state =
            _uiState.value

        if (!manager.fileExists(state.fileName)) {
            return
        }

        viewModelScope.launch {
            val existingFile =
                repository.getFile(
                    state.fileName
                )

            val currentTime =
                System.currentTimeMillis()

            val metadata =
                if (existingFile == null) {
                    EditorFile(
                        fileName = state.fileName,
                        encoding = state.encoding,
                        isReadOnly = isReadOnly,
                        lastOpenedAt = currentTime,
                        lastModifiedAt = currentTime
                    )
                } else {
                    existingFile.copy(
                        isReadOnly = isReadOnly
                    )
                }

            repository.saveFileMetadata(
                metadata
            )
        }
    }

    private fun updateUndoRedoState() {
        _uiState.value =
            _uiState.value.copy(
                canUndo =
                    undoStack.isNotEmpty(),
                canRedo =
                    redoStack.isNotEmpty()
            )
    }

    override fun onCleared() {
        recoveryJob?.cancel()

        super.onCleared()
    }
}