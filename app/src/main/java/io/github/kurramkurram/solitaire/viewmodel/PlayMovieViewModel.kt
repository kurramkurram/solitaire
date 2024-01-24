package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.repository.MovieRepository
import io.github.kurramkurram.solitaire.util.CLICKED_MOVIE_ITEM
import io.github.kurramkurram.solitaire.util.CLICKED_RESET_BUTTON
import io.github.kurramkurram.solitaire.util.Event
import javax.inject.Inject

/**
 * 動画の画面のViewModel.
 */
@HiltViewModel
class PlayMovieViewModel @Inject constructor(
    application: Application,
    private val movieRepository: MovieRepository
) : AndroidViewModel(application) {

    private val _navigation = MutableLiveData<Event<String>>()
    val navigation: LiveData<Event<String>>
        get() = _navigation

    var selectFilePath: String = ""
        private set

    val movieInfo: LiveData<MutableList<Movie>> = movieRepository.getAllMovie()

    /**
     * 動画を表示する.
     *
     * @param fileName ファイル名
     */
    fun showMovie(fileName: String) {
        selectFilePath = movieRepository.getMovieFilePath(fileName)
        if (selectFilePath.isNotBlank()) {
            _navigation.value = Event(CLICKED_MOVIE_ITEM)
        }
    }

    /**
     * リセットダイアログを表示する.
     */
    fun showResetDialog() {
        _navigation.value = Event(CLICKED_RESET_BUTTON)
    }

    fun deleteAll() {
        movieRepository.deleteAllMovie()
    }
}
