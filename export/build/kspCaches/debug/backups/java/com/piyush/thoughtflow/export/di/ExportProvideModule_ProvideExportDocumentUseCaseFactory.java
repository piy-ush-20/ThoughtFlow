package com.piyush.thoughtflow.export.di;

import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import com.piyush.thoughtflow.domain.repository.ExportRepository;
import com.piyush.thoughtflow.domain.usecase.ExportDocumentUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ExportProvideModule_ProvideExportDocumentUseCaseFactory implements Factory<ExportDocumentUseCase> {
  private final Provider<DocumentRepository> docsProvider;

  private final Provider<ExportRepository> exportProvider;

  private ExportProvideModule_ProvideExportDocumentUseCaseFactory(
      Provider<DocumentRepository> docsProvider, Provider<ExportRepository> exportProvider) {
    this.docsProvider = docsProvider;
    this.exportProvider = exportProvider;
  }

  @Override
  public ExportDocumentUseCase get() {
    return provideExportDocumentUseCase(docsProvider.get(), exportProvider.get());
  }

  public static ExportProvideModule_ProvideExportDocumentUseCaseFactory create(
      Provider<DocumentRepository> docsProvider, Provider<ExportRepository> exportProvider) {
    return new ExportProvideModule_ProvideExportDocumentUseCaseFactory(docsProvider, exportProvider);
  }

  public static ExportDocumentUseCase provideExportDocumentUseCase(DocumentRepository docs,
      ExportRepository export) {
    return Preconditions.checkNotNullFromProvides(ExportProvideModule.INSTANCE.provideExportDocumentUseCase(docs, export));
  }
}
