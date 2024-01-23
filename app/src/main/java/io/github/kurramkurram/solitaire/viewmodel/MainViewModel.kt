package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kurramkurram.solitaire.repository.MusicRepository
import io.github.kurramkurram.solitaire.usecase.PlayBgmUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * MainActivityに共通のViewModel.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val musicRepository: MusicRepository
) : AndroidViewModel(application) {

    /**
     * 「音楽をかける」の状態.
     */
    val musicState = MutableLiveData(runBlocking { musicRepository.getPlayChecked() })

    private var mediaPlayer: MediaPlayer? = null

    /**
     * 動画を再生する.
     */
    fun startMusic() {
        val state = runBlocking { musicRepository.getPlayChecked() }
        if (state) {
            mediaPlayer = PlayBgmUseCase(musicRepository).invoke()
            viewModelScope.launch { musicRepository.setPlayChecked(true) }
            musicState.value = true
        }
    }

    /**
     * トグル変更.
     */
    fun onMusicCheckChanged(newValue: Boolean) {
        Log.d("MainViewModel", "#onMusicCheckChanged newValue = $newValue")
        if (newValue) {
            mediaPlayer = PlayBgmUseCase(musicRepository).invoke()
        } else {
            releaseMusic()
        }
        viewModelScope.launch { musicRepository.setPlayChecked(newValue) }
    }

    /**
     * 動画を停止する.
     */
    fun releaseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            musicState.value = false
        }
    }
}
