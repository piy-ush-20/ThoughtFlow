package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import com.piyush.thoughtflow.domain.usecase.ListDocumentsUseCase;
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
public final class DataProvideModule_ProvideListDocumentsFactory implements Factory<ListDocumentsUseCase> {
  private final Provider<DocumentRepository> docsProvider;

  private DataProvideModule_ProvideListDocumentsFactory(Provider<DocumentRepository> docsProvider) {
    this.docsProvider = docsProvider;
  }

  @Override
  public ListDocumentsUseCase get() {
    return provideListDocuments(docsProvider.get());
  }

  public static DataProvideModule_ProvideListDocumentsFactory create(
      Provider<DocumentRepository> docsProvider) {
    return new DataProvideModule_ProvideListDocumentsFactory(docsProvider);
  }

  public static ListDocumentsUseCase provideListDocuments(DocumentRepository docs) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideListDocuments(docs));
  }
}
