package com.ucsc.is2205.moderntexteditor.domain.model

data class FileVersion(
    val id: Long = 0L,
    val fileName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
