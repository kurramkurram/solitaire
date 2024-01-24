package io.github.kurramkurram.solitaire.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kurramkurram.solitaire.repository.DateRepository
import io.github.kurramkurram.solitaire.repository.DateRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DateModule {

    @Provides
    @Singleton
    fun provideDateRepository(): DateRepository = DateRepositoryImpl()
}
