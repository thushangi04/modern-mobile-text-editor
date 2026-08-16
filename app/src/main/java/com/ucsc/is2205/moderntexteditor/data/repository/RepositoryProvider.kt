package com.ucsc.is2205.moderntexteditor.data.repository

import android.content.Context
import com.ucsc.is2205.moderntexteditor.data.local.database.AppDatabase
import com.ucsc.is2205.moderntexteditor.domain.repository.FileRepository
import com.ucsc.is2205.moderntexteditor.domain.repository.VersionRepository

class RepositoryProvider(
    context: Context
) {

    private val database =
        AppDatabase.getInstance(
            context.applicationContext
        )

    val fileRepository: FileRepository =
        FileRepositoryImpl(
            database.fileDao()
        )

    val versionRepository: VersionRepository =
        VersionRepositoryImpl(
            database.versionDao()
        )
}