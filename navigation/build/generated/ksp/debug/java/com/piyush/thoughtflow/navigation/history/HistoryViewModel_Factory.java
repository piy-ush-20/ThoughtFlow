package com.piyush.thoughtflow.navigation.history;

import com.piyush.thoughtflow.domain.usecase.DeleteDocumentUseCase;
import com.piyush.thoughtflow.domain.usecase.ListDocumentsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<ListDocumentsUseCase> listDocumentsProvider;

  private final Provider<DeleteDocumentUseCase> deleteDocumentProvider;

  private HistoryViewModel_Factory(Provider<ListDocumentsUseCase> listDocumentsProvider,
      Provider<DeleteDocumentUseCase> deleteDocumentProvider) {
    this.listDocumentsProvider = listDocumentsProvider;
    this.deleteDocumentProvider = deleteDocumentProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(listDocumentsProvider.get(), deleteDocumentProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<ListDocumentsUseCase> listDocumentsProvider,
      Provider<DeleteDocumentUseCase> deleteDocumentProvider) {
    return new HistoryViewModel_Factory(listDocumentsProvider, deleteDocumentProvider);
  }

  public static HistoryViewModel newInstance(ListDocumentsUseCase listDocuments,
      DeleteDocumentUseCase deleteDocument) {
    return new HistoryViewModel(listDocuments, deleteDocument);
  }
}
