package io.github.kurramkurram.solitaire.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kurramkurram.solitaire.usecase.CreateBarChartUseCase
import io.github.kurramkurram.solitaire.usecase.CreateBarChartUseCaseImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CreateBarChartUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateBarCartUseCase(): CreateBarChartUseCase = CreateBarChartUseCaseImpl()
}
