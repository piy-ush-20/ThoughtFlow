package com.piyush.thoughtflow.export.di

import com.piyush.thoughtflow.domain.repository.ExportRepository
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import com.piyush.thoughtflow.domain.usecase.ExportDocumentUseCase
import com.piyush.thoughtflow.export.ExportRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExportBindModule {
    @Binds
    @Singleton
    abstract fun bindExportRepository(impl: ExportRepositoryImpl): ExportRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ExportProvideModule {
    @Provides
    fun provideExportDocumentUseCase(
        docs: DocumentRepository,
        export: ExportRepository,
    ): ExportDocumentUseCase = ExportDocumentUseCase(docs, export)
}
