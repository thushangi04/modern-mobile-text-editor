package com.ucsc.is2205.moderntexteditor.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ucsc.is2205.moderntexteditor.data.local.database.dao.FileDao
import com.ucsc.is2205.moderntexteditor.data.local.database.dao.VersionDao
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.FileEntity
import com.ucsc.is2205.moderntexteditor.data.local.database.entity.VersionEntity

@Database(
    entities = [
        FileEntity::class,
        VersionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fileDao(): FileDao

    abstract fun versionDao(): VersionDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(
            context: Context
        ): AppDatabase {

            return instance
                ?: synchronized(this) {

                    instance
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "modern_text_editor_database"
                        )
                            .build()
                            .also { database ->
                                instance = database
                            }
                }
        }
    }
}