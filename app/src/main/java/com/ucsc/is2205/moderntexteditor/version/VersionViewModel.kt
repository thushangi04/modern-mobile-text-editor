package com.ucsc.is2205.moderntexteditor.version

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import com.ucsc.is2205.moderntexteditor.domain.repository.VersionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VersionUiState(
    val versions: List<FileVersion> = emptyList(),
    val selectedVersion: FileVersion? = null,
    val compareVersion: FileVersion? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class VersionViewModel(
    private val versionRepository: VersionRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            VersionUiState()
        )

    val uiState: StateFlow<VersionUiState> =
        _uiState.asStateFlow()

    private var currentFileName: String = ""

    /*
     * ---------------------------------------------------------
     * Load versions
     * ---------------------------------------------------------
     */

    fun loadVersions(
        fileName: String
    ) {
        currentFileName = fileName
        
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                val fileVersions = versionRepository.getVersionsForFile(fileName)
                _uiState.value = _uiState.value.copy(
                    versions = fileVersions,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load versions"
                )
            }
        }
    }

    /*
     * ---------------------------------------------------------
     * Add version
     * ---------------------------------------------------------
     *
     * This keeps version history in memory for the current
     * application session. Persistent repository/database
     * integration can be added separately later.
     */

    fun addVersion(
        fileName: String,
        content: String
    ) {
        viewModelScope.launch {
            try {
                val newVersion = FileVersion(
                    fileName = fileName,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                
                val createdVersion = versionRepository.createVersion(newVersion)
                
                val currentVersions = _uiState.value.versions
                _uiState.value = _uiState.value.copy(
                    versions = listOf(createdVersion) + currentVersions,
                    selectedVersion = createdVersion,
                    compareVersion = null,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to save version"
                )
            }
        }
    }

    /*
     * ---------------------------------------------------------
     * Select version
     * ---------------------------------------------------------
     */

    fun selectVersion(
        version: FileVersion
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedVersion = version,
                compareVersion = null
            )
    }

    /*
     * ---------------------------------------------------------
     * Compare selected version with previous version
     * ---------------------------------------------------------
     */

    fun compareWithPrevious() {

        val state =
            _uiState.value

        val selected =
            state.selectedVersion
                ?: return

        val versions =
            state.versions

        val selectedIndex =
            versions.indexOfFirst { version ->
                version.id == selected.id
            }

        if (selectedIndex < 0) {
            return
        }

        /*
         * Versions are stored newest first.
         * Therefore the previous version is the next item.
         */
        val previousIndex =
            selectedIndex + 1

        if (previousIndex >= versions.size) {

            _uiState.value =
                state.copy(
                    compareVersion = null
                )

            return
        }

        _uiState.value =
            state.copy(
                compareVersion =
                    versions[previousIndex]
            )
    }

    /*
     * ---------------------------------------------------------
     * Clear comparison
     * ---------------------------------------------------------
     */

    fun clearComparison() {

        _uiState.value =
            _uiState.value.copy(
                compareVersion = null
            )
    }

    /*
     * ---------------------------------------------------------
     * Clear selected version
     * ---------------------------------------------------------
     */

    fun clearSelection() {

        _uiState.value =
            _uiState.value.copy(
                selectedVersion = null,
                compareVersion = null
            )
    }

    /*
     * ---------------------------------------------------------
     * Clear version history
     * ---------------------------------------------------------
     */

    fun clearHistory() {

        _uiState.value =
            _uiState.value.copy(
                versions = emptyList(),
                selectedVersion = null,
                compareVersion = null,
                errorMessage = null
            )
    }

    /*
     * ---------------------------------------------------------
     * Clear error
     * ---------------------------------------------------------
     */

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    companion object {
        fun provideFactory(
            repository: VersionRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VersionViewModel(repository) as T
            }
        }
    }
}