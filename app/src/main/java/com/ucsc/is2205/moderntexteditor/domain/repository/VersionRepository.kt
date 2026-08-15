package com.ucsc.is2205.moderntexteditor.domain.repository

import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion

interface VersionRepository {

    suspend fun getVersionsForFile(
        fileName: String
    ): List<FileVersion>

    suspend fun createVersion(
        version: FileVersion
    ): FileVersion

    suspend fun getVersionById(
        versionId: Long
    ): FileVersion?

    suspend fun getBaseVersion(
        fileName: String
    ): FileVersion?

    suspend fun deleteVersionsForFile(
        fileName: String
    )
}