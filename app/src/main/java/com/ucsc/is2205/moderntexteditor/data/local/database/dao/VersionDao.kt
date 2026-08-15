package com.ucsc.is2205.moderntexteditor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.VersionEntity

@Dao
interface VersionDao {

    @Insert
    suspend fun insertVersion(
        version: VersionEntity
    ): Long

    @Query(
        "SELECT * FROM file_versions " +
                "WHERE fileName = :fileName " +
                "ORDER BY versionNumber DESC"
    )
    suspend fun getVersionsForFile(
        fileName: String
    ): List<VersionEntity>

    @Query(
        "SELECT * FROM file_versions " +
                "WHERE id = :versionId LIMIT 1"
    )
    suspend fun getVersionById(
        versionId: Long
    ): VersionEntity?

    @Query(
        "SELECT * FROM file_versions " +
                "WHERE fileName = :fileName " +
                "AND isBaseVersion = 1 LIMIT 1"
    )
    suspend fun getBaseVersion(
        fileName: String
    ): VersionEntity?

    @Query(
        "SELECT COALESCE(MAX(versionNumber), 0) + 1 " +
                "FROM file_versions " +
                "WHERE fileName = :fileName"
    )
    suspend fun getNextVersionNumber(
        fileName: String
    ): Int

    @Query(
        "DELETE FROM file_versions " +
                "WHERE fileName = :fileName"
    )
    suspend fun deleteVersionsForFile(
        fileName: String
    )
}