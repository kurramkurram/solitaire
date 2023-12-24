package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.database.MovieDataSource
import io.github.kurramkurram.solitaire.database.MovieDataSourceImpl
import java.io.File

abstract class MovieRepository {

    abstract fun getAllFile(): List<File>

    abstract fun getSaveFile(): File

    abstract fun deleteOldestFile(): Boolean
}

class MovieRepositoryImpl(
    context: Context,
    private val movieDataSource: MovieDataSource = MovieDataSourceImpl(context)
) : MovieRepository() {

    override fun getAllFile(): List<File> = movieDataSource.getAllFile()

    override fun getSaveFile(): File = movieDataSource.getSaveFile()

    override fun deleteOldestFile(): Boolean = movieDataSource.deleteOldestFile()
}