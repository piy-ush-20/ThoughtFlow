package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import com.piyush.thoughtflow.domain.usecase.GetDocumentUseCase;
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
public final class DataProvideModule_ProvideGetDocumentFactory implements Factory<GetDocumentUseCase> {
  private final Provider<DocumentRepository> docsProvider;

  private DataProvideModule_ProvideGetDocumentFactory(Provider<DocumentRepository> docsProvider) {
    this.docsProvider = docsProvider;
  }

  @Override
  public GetDocumentUseCase get() {
    return provideGetDocument(docsProvider.get());
  }

  public static DataProvideModule_ProvideGetDocumentFactory create(
      Provider<DocumentRepository> docsProvider) {
    return new DataProvideModule_ProvideGetDocumentFactory(docsProvider);
  }

  public static GetDocumentUseCase provideGetDocument(DocumentRepository docs) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideGetDocument(docs));
  }
}
