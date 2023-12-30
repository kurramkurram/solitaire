package io.github.kurramkurram.solitaire.database

import android.annotation.SuppressLint
import android.content.Context
import java.io.File

/**
 * 動画のデータソース.
 */
abstract class MovieDataSource {

    /**
     * 動画のパスを取得する.
     *
     * @param fileName ファイル名
     * @return ファイルのパス
     */
    abstract fun getMovieFilePath(fileName: String): String

    /**
     * 保存する動画ファイルを取得する.
     *
     * @return ファイル
     */
    abstract fun getSaveFile(): File

    /**
     * 指定された動画を削除する.
     *
     * @return 成功の真偽
     */
    abstract fun deleteMovieFile(name: String): Boolean
}

class MovieDataSourceImpl(private val context: Context) : MovieDataSource() {
    override fun getMovieFilePath(fileName: String): String =
        "${context.getExternalFilesDir(null)}/$fileName"

    @SuppressLint("SimpleDateFormat")
    override fun getSaveFile(): File {
        val filePath =
            "${context.getExternalFilesDir(null)!!.path}/${System.currentTimeMillis()}.mp4"
        return File(filePath)
    }

    override fun deleteMovieFile(name: String): Boolean {
        val filePath = "${context.getExternalFilesDir(null)!!.path}/$name.mp4"
        return File(filePath).delete()
    }

    companion object {
        private const val TAG = "MovieDataSourceImpl"
    }
}
