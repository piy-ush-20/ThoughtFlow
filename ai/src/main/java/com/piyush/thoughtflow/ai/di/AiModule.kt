package com.piyush.thoughtflow.ai.di

import com.piyush.thoughtflow.ai.AIRepositoryImpl
import com.piyush.thoughtflow.ai.AdaptiveDocumentFormatter
import com.piyush.thoughtflow.ai.AiHttpClient
import com.piyush.thoughtflow.ai.DocumentFormatter
import com.piyush.thoughtflow.domain.repository.AIRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiBindModule {
    @Binds
    @Singleton
    abstract fun bindDocumentFormatter(impl: AdaptiveDocumentFormatter): DocumentFormatter

    @Binds
    @Singleton
    abstract fun bindAIRepository(impl: AIRepositoryImpl): AIRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AiProvideModule {
    @Provides
    @Singleton
    fun provideOkHttp(client: AiHttpClient): OkHttpClient = client.client

    @Provides
    @Singleton
    fun provideJson(client: AiHttpClient): Json = client.json
}
