package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.database.ArchiveDataSource
import io.github.kurramkurram.solitaire.database.ArchiveDataSourceImpl
import java.io.File

abstract class ArchiveRepository {

    abstract fun getDatabaseFile(dbName: String): File

    abstract fun getCompressedFile(fileName: String): File

    abstract fun compressFile(target: File, zipFile: File): File?

    abstract fun decompressFile(zipFile: File, targetParent: String, target: String): Boolean

    abstract fun deleteFile(file: File): Boolean
}

class ArchiveRepositoryImpl(
    private val context: Context,
    private val archiveDataSource: ArchiveDataSource = ArchiveDataSourceImpl(context)
) : ArchiveRepository() {
    override fun getDatabaseFile(dbName: String): File = archiveDataSource.getDatabaseFile(dbName)

    override fun getCompressedFile(fileName: String): File =
        archiveDataSource.getCompressedFile(fileName)

    override fun compressFile(target: File, zipFile: File): File? =
        archiveDataSource.compressFile(target, zipFile)

    override fun decompressFile(zipFile: File, targetParent: String, target: String): Boolean =
        archiveDataSource.decompressFile(zipFile, targetParent, target)

    override fun deleteFile(file: File): Boolean = archiveDataSource.deleteFile(file)
}