package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import com.piyush.thoughtflow.domain.usecase.DeleteDocumentUseCase;
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
public final class DataProvideModule_ProvideDeleteDocumentFactory implements Factory<DeleteDocumentUseCase> {
  private final Provider<DocumentRepository> docsProvider;

  private DataProvideModule_ProvideDeleteDocumentFactory(
      Provider<DocumentRepository> docsProvider) {
    this.docsProvider = docsProvider;
  }

  @Override
  public DeleteDocumentUseCase get() {
    return provideDeleteDocument(docsProvider.get());
  }

  public static DataProvideModule_ProvideDeleteDocumentFactory create(
      Provider<DocumentRepository> docsProvider) {
    return new DataProvideModule_ProvideDeleteDocumentFactory(docsProvider);
  }

  public static DeleteDocumentUseCase provideDeleteDocument(DocumentRepository docs) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideDeleteDocument(docs));
  }
}
