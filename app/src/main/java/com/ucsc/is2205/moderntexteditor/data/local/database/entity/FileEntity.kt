package com.ucsc.is2205.moderntexteditor.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "editor_files")
data class FileEntity(
    @PrimaryKey
    val fileName: String,
    val encoding: String = "UTF-8",
    val isReadOnly: Boolean = false,
    val lastOpenedAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)