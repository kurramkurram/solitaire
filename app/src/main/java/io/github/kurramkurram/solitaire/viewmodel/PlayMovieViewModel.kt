package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kurramkurram.solitaire.repository.MovieRepositoryImpl
import io.github.kurramkurram.solitaire.util.Event
import java.io.File


class PlayMovieViewModel(private val application: Application) : AndroidViewModel(application) {
    private val movieRepository = MovieRepositoryImpl(application.applicationContext)

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation: LiveData<Event<String>>
        get() = _navigation

    private val _movieItems = MutableLiveData(movieRepository.getAllFile())
    val movieItems: LiveData<List<File>>
        get() = _movieItems

    var selectFile: File? = null

    fun showMovie(fileName: String) {
        selectFile = movieItems.value!!.find { file -> file.name == fileName }
        selectFile?.let {
            _navigation.value = Event("ShowMovie")
        }
    }

    fun updateMovie() {
        _movieItems.value = movieRepository.getAllFile()
    }
}
