package com.ucsc.is2205.moderntexteditor.domain.model

data class EditorFile(
    val fileName: String,
    val encoding: String = "UTF-8",
    val isReadOnly: Boolean = false,
    val lastOpenedAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis()
)