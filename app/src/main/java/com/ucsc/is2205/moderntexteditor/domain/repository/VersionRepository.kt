package com.ucsc.is2205.moderntexteditor.domain.repository

import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion

interface VersionRepository {

    fun getVersionsForFile(
        fileName: String
    ): List<FileVersion>

    fun createVersion(
        version: FileVersion
    ): FileVersion

    fun deleteVersionsForFile(
        fileName: String
    )
}
