package io.github.kurramkurram.solitaire.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kurramkurram.solitaire.repository.RecordRepository
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RecordModule {

    @Provides
    @Singleton
    fun provideRecordRepository(@ApplicationContext context: Context): RecordRepository =
        RecordRepositoryImpl(context)
}
