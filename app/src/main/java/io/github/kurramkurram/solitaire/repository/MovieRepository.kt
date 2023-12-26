package io.github.kurramkurram.solitaire.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.database.MovieDataSource
import io.github.kurramkurram.solitaire.database.MovieDataSourceImpl
import io.github.kurramkurram.solitaire.database.RecordDatabase
import java.io.File

abstract class MovieRepository {

    /**
     * 動画のパスを取得する.
     */
    abstract fun getMovieFilePath(fileName: String): String

    /**
     * 保存する動画ファイルを取得する.
     */
    abstract fun getSaveFile(): File

    /**
     * すべての動画の情報を取得する.
     */
    abstract fun getAllMovie(): LiveData<MutableList<Movie>>

    /**
     * 動画の情報を保存する.
     */
    abstract fun saveMovieInfo(movie: Movie)

    /**
     * 最も動画を削除する.
     */
    abstract fun deleteOldestMovie(): Boolean
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
        if (count < 7) return true
        val fileName = dao.selectDeleteMovie()
        dao.deleteOldest(fileName)
        return movieDataSource.deleteOldestFile(fileName)
    }
}