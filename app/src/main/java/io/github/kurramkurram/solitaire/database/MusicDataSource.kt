package io.github.kurramkurram.solitaire.database

import android.content.Context
import android.content.res.AssetFileDescriptor
import io.github.kurramkurram.solitaire.util.MUSIC_PLAY_CHECK_KEY
import io.github.kurramkurram.solitaire.util.MusicType
import io.github.kurramkurram.solitaire.util.getPreference
import io.github.kurramkurram.solitaire.util.setPreference
import kotlinx.coroutines.flow.first

/**
 * 音楽のデータソース.
 */
abstract class MusicDataSource {

    /**
     * 音楽を取得する.
     */
    abstract fun getMusic(musicType: MusicType): AssetFileDescriptor

    /**
     * 「音楽をかける」の状態を取得.
     */
    abstract suspend fun getPlayChecked(): Boolean

    /**
     * 「音楽をかける」の状態を保存.
     */
    abstract suspend fun setPlayChecked(state: Boolean)
}

class MusicDataSourceImpl(private val context: Context) : MusicDataSource() {

    override fun getMusic(musicType: MusicType): AssetFileDescriptor =
        context.assets.openFd("music/${musicType.fileName}")

    override suspend fun getPlayChecked(): Boolean =
        getPreference(context, MUSIC_PLAY_CHECK_KEY, true).first()

    override suspend fun setPlayChecked(state: Boolean) {
        setPreference(context, MUSIC_PLAY_CHECK_KEY, state)
    }
}
