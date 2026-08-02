package com.piyush.thoughtflow.editor;

import androidx.lifecycle.SavedStateHandle;
import com.piyush.thoughtflow.domain.usecase.ExportDocumentUseCase;
import com.piyush.thoughtflow.domain.usecase.GetDocumentUseCase;
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase;
import com.piyush.thoughtflow.export.ExportRepositoryImpl;
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
public final class EditorViewModel_Factory implements Factory<EditorViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetDocumentUseCase> getDocumentProvider;

  private final Provider<SaveDocumentUseCase> saveDocumentProvider;

  private final Provider<ExportDocumentUseCase> exportDocumentProvider;

  private final Provider<ExportRepositoryImpl> exportRepositoryProvider;

  private EditorViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetDocumentUseCase> getDocumentProvider,
      Provider<SaveDocumentUseCase> saveDocumentProvider,
      Provider<ExportDocumentUseCase> exportDocumentProvider,
      Provider<ExportRepositoryImpl> exportRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getDocumentProvider = getDocumentProvider;
    this.saveDocumentProvider = saveDocumentProvider;
    this.exportDocumentProvider = exportDocumentProvider;
    this.exportRepositoryProvider = exportRepositoryProvider;
  }

  @Override
  public EditorViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getDocumentProvider.get(), saveDocumentProvider.get(), exportDocumentProvider.get(), exportRepositoryProvider.get());
  }

  public static EditorViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetDocumentUseCase> getDocumentProvider,
      Provider<SaveDocumentUseCase> saveDocumentProvider,
      Provider<ExportDocumentUseCase> exportDocumentProvider,
      Provider<ExportRepositoryImpl> exportRepositoryProvider) {
    return new EditorViewModel_Factory(savedStateHandleProvider, getDocumentProvider, saveDocumentProvider, exportDocumentProvider, exportRepositoryProvider);
  }

  public static EditorViewModel newInstance(SavedStateHandle savedStateHandle,
      GetDocumentUseCase getDocument, SaveDocumentUseCase saveDocument,
      ExportDocumentUseCase exportDocument, ExportRepositoryImpl exportRepository) {
    return new EditorViewModel(savedStateHandle, getDocument, saveDocument, exportDocument, exportRepository);
  }
}
