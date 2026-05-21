package com.piyush.thoughtflow.data.di

import android.content.Context
import androidx.room.Room
import com.piyush.thoughtflow.data.local.DocumentDao
import com.piyush.thoughtflow.data.local.ThoughtFlowDatabase
import com.piyush.thoughtflow.data.repository.DocumentRepositoryImpl
import com.piyush.thoughtflow.data.repository.SettingsRepositoryImpl
import com.piyush.thoughtflow.domain.repository.AIRepository
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import com.piyush.thoughtflow.domain.repository.SpeechRepository
import com.piyush.thoughtflow.domain.usecase.DeleteDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.FormatTranscriptUseCase
import com.piyush.thoughtflow.domain.usecase.GetDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.ListDocumentsUseCase
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.StartListeningUseCase
import com.piyush.thoughtflow.domain.usecase.StopListeningUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindModule {
    @Binds
    @Singleton
    abstract fun bindDocumentRepository(impl: DocumentRepositoryImpl): DocumentRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataProvideModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ThoughtFlowDatabase =
        Room.databaseBuilder(context, ThoughtFlowDatabase::class.java, "thoughtflow.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideDocumentDao(db: ThoughtFlowDatabase): DocumentDao = db.documentDao()

    @Provides
    fun provideStartListening(speech: SpeechRepository) = StartListeningUseCase(speech)

    @Provides
    fun provideStopListening(speech: SpeechRepository) = StopListeningUseCase(speech)

    @Provides
    fun provideFormatTranscript(ai: AIRepository) = FormatTranscriptUseCase(ai)

    @Provides
    fun provideSaveDocument(docs: DocumentRepository) = SaveDocumentUseCase(docs)

    @Provides
    fun provideListDocuments(docs: DocumentRepository) = ListDocumentsUseCase(docs)

    @Provides
    fun provideGetDocument(docs: DocumentRepository) = GetDocumentUseCase(docs)

    @Provides
    fun provideDeleteDocument(docs: DocumentRepository) = DeleteDocumentUseCase(docs)
}
