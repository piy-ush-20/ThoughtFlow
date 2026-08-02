package com.piyush.thoughtflow.speech.di

import com.piyush.thoughtflow.domain.repository.SpeechRepository
import com.piyush.thoughtflow.speech.AndroidSpeechRecognizerEngine
import com.piyush.thoughtflow.speech.SpeechEngine
import com.piyush.thoughtflow.speech.SpeechRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {
    @Binds
    @Singleton
    abstract fun bindSpeechEngine(impl: AndroidSpeechRecognizerEngine): SpeechEngine

    @Binds
    @Singleton
    abstract fun bindSpeechRepository(impl: SpeechRepositoryImpl): SpeechRepository
}
