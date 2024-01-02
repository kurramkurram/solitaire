package io.github.kurramkurram.solitaire.repository

import android.content.Context
import android.content.res.AssetFileDescriptor
import io.github.kurramkurram.solitaire.database.MusicDataSource
import io.github.kurramkurram.solitaire.database.MusicDataSourceImpl
import io.github.kurramkurram.solitaire.util.MusicType

/**
 * 音楽のリポジトリ.
 */
abstract class MusicRepository {

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

class MusicRepositoryImpl(
    context: Context,
    private val musicDataSource: MusicDataSource = MusicDataSourceImpl(context)
) : MusicRepository() {

    override fun getMusic(musicType: MusicType): AssetFileDescriptor =
        musicDataSource.getMusic(musicType)

    override suspend fun getPlayChecked(): Boolean = musicDataSource.getPlayChecked()

    override suspend fun setPlayChecked(state: Boolean) = musicDataSource.setPlayChecked(state)
}
