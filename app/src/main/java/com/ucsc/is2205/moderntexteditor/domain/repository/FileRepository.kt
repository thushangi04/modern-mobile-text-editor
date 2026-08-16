package com.ucsc.is2205.moderntexteditor.domain.repository

import com.ucsc.is2205.moderntexteditor.domain.model.EditorFile

interface FileRepository {

    suspend fun saveFileMetadata(
        file: EditorFile
    )

    suspend fun getFile(
        fileName: String
    ): EditorFile?

    suspend fun getRecentFiles(
        limit: Int = 10
    ): List<EditorFile>

    suspend fun updateReadOnlyStatus(
        fileName: String,
        isReadOnly: Boolean
    )

    suspend fun deleteFileMetadata(
        fileName: String
    )
}