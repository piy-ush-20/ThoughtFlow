package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase;
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
public final class DataProvideModule_ProvideSaveDocumentFactory implements Factory<SaveDocumentUseCase> {
  private final Provider<DocumentRepository> docsProvider;

  private DataProvideModule_ProvideSaveDocumentFactory(Provider<DocumentRepository> docsProvider) {
    this.docsProvider = docsProvider;
  }

  @Override
  public SaveDocumentUseCase get() {
    return provideSaveDocument(docsProvider.get());
  }

  public static DataProvideModule_ProvideSaveDocumentFactory create(
      Provider<DocumentRepository> docsProvider) {
    return new DataProvideModule_ProvideSaveDocumentFactory(docsProvider);
  }

  public static SaveDocumentUseCase provideSaveDocument(DocumentRepository docs) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideSaveDocument(docs));
  }
}
