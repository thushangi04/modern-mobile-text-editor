package com.ucsc.is2205.moderntexteditor.data.repository

import com.ucsc.is2205.moderntexteditor.data.local.database.dao.FileDao
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.FileEntity
import com.ucsc.is2205.moderntexteditor.domain.model.EditorFile
import com.ucsc.is2205.moderntexteditor.domain.repository.FileRepository

class FileRepositoryImpl(
    private val fileDao: FileDao
) : FileRepository {

    override suspend fun saveFileMetadata(
        file: EditorFile
    ) {
        fileDao.upsertFile(
            file.toEntity()
        )
    }

    override suspend fun getFile(
        fileName: String
    ): EditorFile? {
        return fileDao
            .getFile(fileName)
            ?.toDomainModel()
    }

    override suspend fun getRecentFiles(
        limit: Int
    ): List<EditorFile> {
        return fileDao
            .getRecentFiles(limit)
            .map { entity ->
                entity.toDomainModel()
            }
    }

    override suspend fun updateReadOnlyStatus(
        fileName: String,
        isReadOnly: Boolean
    ) {
        fileDao.updateReadOnlyStatus(
            fileName = fileName,
            isReadOnly = isReadOnly
        )
    }

    override suspend fun deleteFileMetadata(
        fileName: String
    ) {
        fileDao.deleteFile(fileName)
    }

    private fun EditorFile.toEntity(): FileEntity {
        return FileEntity(
            fileName = fileName,
            encoding = encoding,
            isReadOnly = isReadOnly,
            lastOpenedAt = lastOpenedAt,
            lastModifiedAt = lastModifiedAt
        )
    }

    private fun FileEntity.toDomainModel(): EditorFile {
        return EditorFile(
            fileName = fileName,
            encoding = encoding,
            isReadOnly = isReadOnly,
            lastOpenedAt = lastOpenedAt,
            lastModifiedAt = lastModifiedAt
        )
    }
}