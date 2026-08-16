package com.ucsc.is2205.moderntexteditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.FileEntity

@Dao
interface FileDao {

    @Upsert
    suspend fun upsertFile(
        file: FileEntity
    )

    @Query(
        "SELECT * FROM editor_files " +
                "WHERE fileName = :fileName LIMIT 1"
    )
    suspend fun getFile(
        fileName: String
    ): FileEntity?

    @Query(
        "SELECT * FROM editor_files " +
                "ORDER BY lastOpenedAt DESC LIMIT :limit"
    )
    suspend fun getRecentFiles(
        limit: Int = 10
    ): List<FileEntity>

    @Query(
        "UPDATE editor_files " +
                "SET isReadOnly = :isReadOnly " +
                "WHERE fileName = :fileName"
    )
    suspend fun updateReadOnlyStatus(
        fileName: String,
        isReadOnly: Boolean
    )

    @Query(
        "DELETE FROM editor_files " +
                "WHERE fileName = :fileName"
    )
    suspend fun deleteFile(
        fileName: String
    )
}