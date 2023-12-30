package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.database.ArchiveDataSource
import io.github.kurramkurram.solitaire.database.ArchiveDataSourceImpl
import java.io.File

/**
 * データの保存・復元のリポジトリ.
 */
abstract class ArchiveRepository {

    /**
     * Databaseファイルを取得.
     *
     * @param dbName　Database名
     * @return DBファイル
     */
    abstract fun getDatabaseFile(dbName: String): File

    /**
     * 圧縮されているファイルを取得する.
     *
     * @param fileName ファイル名
     * @return 圧縮ファイル
     */
    abstract fun getCompressedFile(fileName: String): File

    /**
     * ファイルを圧縮する.
     *
     * @param target 圧縮対象ファイル
     * @param zipFile 圧縮したいファイル
     * @return 圧縮ファイル
     */
    abstract fun compressFile(target: File, zipFile: File): File?

    /**
     * ファイルを解凍する.
     *
     * @param zipFile 圧縮るするファイル
     * @param targetParent 復元するファイルのディレクトリ
     * @param target 復元するファイル名
     * @return 解凍の成否
     */
    abstract fun decompressFile(zipFile: File, targetParent: String, target: String): Boolean

    /**
     * ファイルを削除する.
     *
     * @param file 削除対称ファイル
     * @return 削除の成否
     */
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
