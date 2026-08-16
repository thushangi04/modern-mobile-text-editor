package com.ucsc.is2205.moderntexteditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "file_versions",
    indices = [
        Index(
            value = ["fileName", "versionNumber"],
            unique = true
        )
    ]
)
data class VersionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fileName: String,
    val versionNumber: Int,
    val label: String? = null,
    val patchData: String,
    val isBaseVersion: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)