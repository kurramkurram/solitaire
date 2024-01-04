package io.github.kurramkurram.solitaire.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.database.MovieDataSource
import io.github.kurramkurram.solitaire.database.MovieDataSourceImpl
import io.github.kurramkurram.solitaire.database.RecordDatabase
import io.github.kurramkurram.solitaire.util.MOVIE_SAVE_COUNT
import java.io.File

/**
 * 動画のリポジトリ.
 */
abstract class MovieRepository {

    /**
     * 動画のパスを取得する.
     *
     * @param fileName ファイル名
     * @return ファイルパス
     */
    abstract fun getMovieFilePath(fileName: String): String

    /**
     * 保存する動画ファイルを取得する.
     *
     * @return 動画ファイル
     */
    abstract fun getSaveFile(): File

    /**
     * すべての動画の情報を取得する.
     *
     * @return 動画情報
     */
    abstract fun getAllMovie(): LiveData<MutableList<Movie>>

    /**
     * 動画の情報を保存する.
     *
     * @param movie 動画情報
     */
    abstract fun saveMovieInfo(movie: Movie)

    /**
     * 最も動画を削除する.
     *
     * @return 削除の成否
     */
    abstract fun deleteOldestMovie(): Boolean

    /**
     * すべての動画を削除する.
     *
     * @return 削除の成否
     */
    abstract fun deleteAllMovie(): Boolean
}

class MovieRepositoryImpl(
    context: Context,
    private val movieDataSource: MovieDataSource = MovieDataSourceImpl(context),
    private val db: RecordDatabase = RecordDatabase.getDatabases(context)
) : MovieRepository() {

    override fun getMovieFilePath(fileName: String): String =
        movieDataSource.getMovieFilePath(fileName)

    override fun getSaveFile(): File = movieDataSource.getSaveFile()

    override fun getAllMovie(): LiveData<MutableList<Movie>> {
        val dao = db.movieDao()
        return dao.selectAll()
    }

    override fun saveMovieInfo(movie: Movie) {
        val dao = db.movieDao()
        return dao.insert(movie)
    }

    override fun deleteOldestMovie(): Boolean {
        val dao = db.movieDao()
        val count = dao.selectCount()
        if (count <= MOVIE_SAVE_COUNT) return true
        val fileName = dao.selectDeleteMovie()
        dao.deleteOldest(fileName)
        return movieDataSource.deleteMovieFile(fileName)
    }

    override fun deleteAllMovie(): Boolean {
        val dao = db.movieDao()
        val fileNames = dao.selectAllFileName()
        dao.deleteAll()
        var result = true
        fileNames.forEach {
            val ret = movieDataSource.deleteMovieFile(it)
            if (!ret) {
                result = false
            }
        }
        return result
    }
}
