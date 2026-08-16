package com.ucsc.is2205.moderntexteditor.data.repository

import com.ucsc.is2205.moderntexteditor.data.local.database.dao.VersionDao
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.VersionEntity
import com.ucsc.is2205.moderntexteditor.domain.model.FileVersion
import com.ucsc.is2205.moderntexteditor.domain.repository.VersionRepository
import com.ucsc.is2205.moderntexteditor.version.DeltaGenerator

class VersionRepositoryImpl(
    private val versionDao: VersionDao
) : VersionRepository {

    override suspend fun getVersionsForFile(
        fileName: String
    ): List<FileVersion> {
        val entities = versionDao.getVersionsForFile(fileName)
        if (entities.isEmpty()) return emptyList()

        val baseEntity = entities.find { it.isBaseVersion }
            ?: return entities.map { it.toDomainModel(it.patchData) }

        val baseContent = baseEntity.patchData
        val orderedEntities = entities.sortedBy { it.versionNumber }
        
        val reconstructedContents = mutableMapOf<Long, String>()
        var currentContent = baseContent
        
        for (entity in orderedEntities) {
            if (entity.isBaseVersion) {
                reconstructedContents[entity.id] = baseContent
            } else {
                currentContent = DeltaGenerator.applyPatch(currentContent, entity.patchData)
                reconstructedContents[entity.id] = currentContent
            }
        }

        return entities.map { entity ->
            entity.toDomainModel(reconstructedContents[entity.id] ?: entity.patchData)
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

        val baseVersion = versionDao.getBaseVersion(version.fileName)
        val isBase = baseVersion == null

        val patchData = if (isBase) {
            version.content
        } else {
            val allVersions = getVersionsForFile(version.fileName)
            val latestVersion = allVersions.maxByOrNull { it.versionNumber }
            
            if (latestVersion != null) {
                DeltaGenerator.computePatch(latestVersion.content, version.content)
            } else {
                version.content
            }
        }

        val entity =
            VersionEntity(
                id = version.id,
                fileName = version.fileName,
                versionNumber = number,
                label = version.label,
                patchData = patchData,
                isBaseVersion = isBase,
                timestamp = version.timestamp
            )

        val generatedId =
            versionDao.insertVersion(entity)

        return version.copy(
            id = generatedId,
            versionNumber = number,
            isBaseVersion = isBase
        )
    }

    override suspend fun getVersionById(
        versionId: Long
    ): FileVersion? {
        val targetEntity = versionDao.getVersionById(versionId) ?: return null
        
        if (targetEntity.isBaseVersion) {
            return targetEntity.toDomainModel(targetEntity.patchData)
        }
        
        val entities = versionDao.getVersionsForFile(targetEntity.fileName)
        val baseEntity = entities.find { it.isBaseVersion } 
            ?: return targetEntity.toDomainModel(targetEntity.patchData)
        
        val orderedEntities = entities
            .filter { it.versionNumber <= targetEntity.versionNumber }
            .sortedBy { it.versionNumber }
        
        var currentContent = baseEntity.patchData
        for (entity in orderedEntities) {
            if (!entity.isBaseVersion) {
                currentContent = DeltaGenerator.applyPatch(currentContent, entity.patchData)
            }
        }
        
        return targetEntity.toDomainModel(currentContent)
    }

    override suspend fun getBaseVersion(
        fileName: String
    ): FileVersion? {
        return versionDao
            .getBaseVersion(fileName)
            ?.let { it.toDomainModel(it.patchData) }
    }

    override suspend fun deleteVersionsForFile(
        fileName: String
    ) {
        versionDao.deleteVersionsForFile(fileName)
    }

    private fun VersionEntity.toDomainModel(content: String): FileVersion {
        return FileVersion(
            id = id,
            fileName = fileName,
            content = content,
            timestamp = timestamp,
            versionNumber = versionNumber,
            label = label,
            isBaseVersion = isBaseVersion
        )
    }
}