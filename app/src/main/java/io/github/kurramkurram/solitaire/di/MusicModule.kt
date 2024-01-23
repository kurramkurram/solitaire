package io.github.kurramkurram.solitaire.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kurramkurram.solitaire.repository.MusicRepository
import io.github.kurramkurram.solitaire.repository.MusicRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MusicModule {

    @Provides
    @Singleton
    fun provideMusicRepository(@ApplicationContext context: Context): MusicRepository =
        MusicRepositoryImpl(context)
}
