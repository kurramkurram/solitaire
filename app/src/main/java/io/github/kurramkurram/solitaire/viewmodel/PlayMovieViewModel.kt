package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.repository.MovieRepositoryImpl
import io.github.kurramkurram.solitaire.util.Event
import java.io.File


class PlayMovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieRepository = MovieRepositoryImpl(application.applicationContext)

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation: LiveData<Event<String>>
        get() = _navigation

    var selectFilePath: String = ""
        private set

    val movieInfo: LiveData<MutableList<Movie>> = movieRepository.getAllMovie()

    fun showMovie(fileName: String) {
        selectFilePath = movieRepository.getMovieFilePath(fileName)
        if (selectFilePath.isNotBlank()) {
            _navigation.value = Event("ShowMovie")
        }
    }
}
