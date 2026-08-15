package com.ucsc.is2205.moderntexteditor.data.repository

import com.ucsc.is2205.moderntexteditor.data.local.database.dao.VersionDao
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.VersionEntity
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import com.ucsc.is2205.moderntexteditor.domain.repository.VersionRepository

class VersionRepositoryImpl(
    private val versionDao: VersionDao
) : VersionRepository {

    override suspend fun getVersionsForFile(
        fileName: String
    ): List<FileVersion> {
        return versionDao
            .getVersionsForFile(fileName)
            .map { entity ->
                entity.toDomainModel()
            }
    }

    override suspend fun createVersion(
        version: FileVersion
    ): FileVersion {

        val number =
            if (version.versionNumber > 0) {
                version.versionNumber
            } else {
                versionDao.getNextVersionNumber(
                    version.fileName
                )
            }

        val entity =
            VersionEntity(
                id = version.id,
                fileName = version.fileName,
                versionNumber = number,
                label = version.label,
                patchData = version.content,
                isBaseVersion = version.isBaseVersion,
                timestamp = version.timestamp
            )

        val generatedId =
            versionDao.insertVersion(entity)

        return version.copy(
            id = generatedId,
            versionNumber = number
        )
    }

    override suspend fun getVersionById(
        versionId: Long
    ): FileVersion? {
        return versionDao
            .getVersionById(versionId)
            ?.toDomainModel()
    }

    override suspend fun getBaseVersion(
        fileName: String
    ): FileVersion? {
        return versionDao
            .getBaseVersion(fileName)
            ?.toDomainModel()
    }

    override suspend fun deleteVersionsForFile(
        fileName: String
    ) {
        versionDao.deleteVersionsForFile(fileName)
    }

    private fun VersionEntity.toDomainModel(): FileVersion {
        return FileVersion(
            id = id,
            fileName = fileName,
            content = patchData,
            timestamp = timestamp,
            versionNumber = versionNumber,
            label = label,
            isBaseVersion = isBaseVersion
        )
    }
}