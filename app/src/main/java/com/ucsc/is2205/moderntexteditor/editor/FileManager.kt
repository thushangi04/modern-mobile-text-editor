package com.ucsc.is2205.moderntexteditor.editor

import android.content.Context
import java.io.File

class FileManager(
    private val context: Context
) {

    companion object {
        private const val RECOVERY_FILE_NAME = ".editor_recovery.txt"
        private const val RECENT_FILES_FILE_NAME = ".recent_files"
        private const val MAX_RECENT_FILES = 10
    }

    private val filesDirectory: File
        get() = context.filesDir

    private val recoveryFile: File
        get() = File(
            filesDirectory,
            RECOVERY_FILE_NAME
        )

    private val recentFilesFile: File
        get() = File(
            filesDirectory,
            RECENT_FILES_FILE_NAME
        )

    /*
     * ---------------------------------------------------------
     * Normal file operations
     * ---------------------------------------------------------
     */

    fun saveFile(
        fileName: String,
        content: String
    ): Boolean {

        return try {

            val safeFileName =
                sanitizeFileName(fileName)

            if (safeFileName.isBlank()) {
                return false
            }

            val file =
                File(
                    filesDirectory,
                    safeFileName
                )

            file.writeText(
                content,
                Charsets.UTF_8
            )

            addToRecentFiles(
                safeFileName
            )

            true

        } catch (
            exception: Exception
        ) {

            false
        }
    }

    /*
     * Compatibility method used by EditorViewModel.
     */
    fun saveAs(
        fileName: String,
        content: String
    ): Boolean {

        return saveFile(
            fileName = fileName,
            content = content
        )
    }

    fun readFile(
        fileName: String
    ): String? {

        return try {

            val safeFileName =
                sanitizeFileName(fileName)

            if (safeFileName.isBlank()) {
                return null
            }

            val file =
                File(
                    filesDirectory,
                    safeFileName
                )

            if (!file.exists()) {
                return null
            }

            val content =
                file.readText(
                    Charsets.UTF_8
                )

            addToRecentFiles(
                safeFileName
            )

            content

        } catch (
            exception: Exception
        ) {

            null
        }
    }

    /*
     * Compatibility method used by EditorViewModel.
     */
    fun openFile(
        fileName: String
    ): String? {

        return readFile(
            fileName
        )
    }

    fun fileExists(
        fileName: String
    ): Boolean {

        return try {

            val safeFileName =
                sanitizeFileName(fileName)

            if (safeFileName.isBlank()) {
                return false
            }

            File(
                filesDirectory,
                safeFileName
            ).exists()

        } catch (
            exception: Exception
        ) {

            false
        }
    }

    /*
     * ---------------------------------------------------------
     * Saved files
     * ---------------------------------------------------------
     */

    fun getSavedFiles(): List<String> {

        return try {

            filesDirectory
                .listFiles()
                ?.filter { file ->

                    file.isFile &&
                            file.name != RECOVERY_FILE_NAME &&
                            file.name != RECENT_FILES_FILE_NAME &&
                            !file.name.startsWith(".")
                }
                ?.map { file ->
                    file.name
                }
                ?.sorted()
                ?: emptyList()

        } catch (
            exception: Exception
        ) {

            emptyList()
        }
    }

    /*
     * Compatibility method used by older EditorViewModel code.
     */
    fun getFileNames(): List<String> {

        return getSavedFiles()
    }

    /*
     * ---------------------------------------------------------
     * Recent files
     * ---------------------------------------------------------
     */

    fun getRecentFiles(): List<String> {

        return try {

            if (!recentFilesFile.exists()) {
                return emptyList()
            }

            recentFilesFile
                .readLines(
                    Charsets.UTF_8
                )
                .filter { fileName ->

                    fileName.isNotBlank() &&
                            fileExists(fileName)
                }
                .distinct()
                .take(MAX_RECENT_FILES)

        } catch (
            exception: Exception
        ) {

            emptyList()
        }
    }

    private fun addToRecentFiles(
        fileName: String
    ) {

        try {

            val existingFiles =
                if (recentFilesFile.exists()) {

                    recentFilesFile
                        .readLines(
                            Charsets.UTF_8
                        )

                } else {

                    emptyList()
                }

            val updatedFiles =
                buildList {

                    add(fileName)

                    existingFiles
                        .filter {
                            it != fileName
                        }
                        .forEach {
                            add(it)
                        }
                }
                    .take(MAX_RECENT_FILES)

            recentFilesFile.writeText(
                updatedFiles.joinToString("\n"),
                Charsets.UTF_8
            )

        } catch (
            exception: Exception
        ) {

            // Recent-file tracking should never
            // prevent normal file operations.
        }
    }

    /*
     * ---------------------------------------------------------
     * Auto-save / recovery
     * ---------------------------------------------------------
     */

    fun saveRecovery(
        text: String
    ): Boolean {

        return try {

            recoveryFile.writeText(
                text,
                Charsets.UTF_8
            )

            true

        } catch (
            exception: Exception
        ) {

            false
        }
    }

    fun loadRecovery(): String? {

        return try {

            if (!recoveryFile.exists()) {
                return null
            }

            recoveryFile.readText(
                Charsets.UTF_8
            )

        } catch (
            exception: Exception
        ) {

            null
        }
    }

    fun hasRecovery(): Boolean {

        return try {

            recoveryFile.exists()

        } catch (
            exception: Exception
        ) {

            false
        }
    }

    fun clearRecovery(): Boolean {

        return try {

            if (!recoveryFile.exists()) {
                true
            } else {
                recoveryFile.delete()
            }

        } catch (
            exception: Exception
        ) {

            false
        }
    }

    /*
     * ---------------------------------------------------------
     * File name validation
     * ---------------------------------------------------------
     */

    private fun sanitizeFileName(
        fileName: String
    ): String {

        return fileName
            .trim()
            .replace(
                Regex("[\\\\/:*?\"<>|]"),
                "_"
            )
            .take(255)
    }
}
