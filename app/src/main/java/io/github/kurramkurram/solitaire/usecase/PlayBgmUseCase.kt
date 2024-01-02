package io.github.kurramkurram.solitaire.usecase

import android.media.MediaPlayer
import io.github.kurramkurram.solitaire.repository.MusicRepository
import io.github.kurramkurram.solitaire.util.MusicType

/**
 * 音楽をかける.
 *
 * @param musicRepository 音楽Repository
 */
class PlayBgmUseCase(
    private val musicRepository: MusicRepository
) {

    fun invoke(): MediaPlayer = MediaPlayer().apply {
        val music = musicRepository.getMusic(MusicType.ELECTRIC)
        setDataSource(music.fileDescriptor, music.startOffset, music.length)
        isLooping = true
        prepare()
        start()
    }
}
