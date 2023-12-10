package io.github.kurramkurram.solitaire.database

import android.content.Context
import io.github.kurramkurram.solitaire.util.L
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

abstract class ArchiveDataSource {

    /**
     * Databaseファイルを取得.
     */
    abstract fun getDatabaseFile(dbName: String): File

    /**
     * 圧縮されているファイルを取得する.
     */
    abstract fun getCompressedFile(fileName: String): File

    /**
     * ファイルを圧縮する.
     */
    abstract fun compressFile(target: File, zipFile: File): File?

    /**
     * ファイルを解凍する.
     */
    abstract fun decompressFile(zipFile: File, targetParent: String, target: String): Boolean

    /**
     * ファイルを削除する.
     */
    abstract fun deleteFile(file: File): Boolean
}

class ArchiveDataSourceImpl(
    private val context: Context,
) : ArchiveDataSource() {

    override fun getDatabaseFile(dbName: String): File = context.getDatabasePath(dbName)

    override fun getCompressedFile(fileName: String): File {
        val cacheDir = context.externalCacheDir
        return File("$cacheDir/$fileName")
    }

    override fun compressFile(target: File, zipFile: File): File? {
        try {
            val buf = ByteArray(1024)
            ZipOutputStream(zipFile.outputStream()).use { outputStream ->
                outputStream.setMethod(ZipOutputStream.DEFLATED)
                val entry = ZipEntry(target.name)
                outputStream.putNextEntry(entry)

                FileInputStream(target).use { inputStream ->
                    var len: Int
                    while (inputStream.read(buf).also { len = it } != -1) {
                        outputStream.write(buf, 0, len)
                        outputStream.flush()
                    }
                }
            }
            return zipFile
        } catch (e: Exception) {
            L.e(TAG, "#compressFile $e")
            return null
        }
    }

    // TODO 解凍できない
    override fun decompressFile(zipFile: File, targetParent: String, target: String): Boolean {
        return try {
            ZipInputStream(Files.newInputStream(Paths.get(zipFile.path))).use { zis ->
                var entry = zis.nextEntry
                while (entry != null) {
                    val dst = File(targetParent, TmpRecordDatabase.DB_NAME)
                    dst.parentFile?.mkdirs()
                    zis.copyTo(BufferedOutputStream(FileOutputStream(dst)))
                    entry = zis.nextEntry
                }
            }
            true
        } catch (e: Exception) {
            L.e(TAG, "#decompressFile $e")
            false
        }
    }

    override fun deleteFile(file: File): Boolean {
        return false
    }

    companion object {
        private const val TAG = "ArchiveDataSourceImpl"
    }
}