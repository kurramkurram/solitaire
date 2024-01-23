package io.github.kurramkurram.solitaire.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kurramkurram.solitaire.repository.MovieRepository
import io.github.kurramkurram.solitaire.repository.MovieRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MovieModule {

    @Provides
    @Singleton
    fun provideMovieRepository(@ApplicationContext context: Context): MovieRepository =
        MovieRepositoryImpl(context)
}
