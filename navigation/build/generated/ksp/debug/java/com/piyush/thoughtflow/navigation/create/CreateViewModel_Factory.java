package com.piyush.thoughtflow.navigation.create;

import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase;
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
public final class CreateViewModel_Factory implements Factory<CreateViewModel> {
  private final Provider<SaveDocumentUseCase> saveDocumentProvider;

  private CreateViewModel_Factory(Provider<SaveDocumentUseCase> saveDocumentProvider) {
    this.saveDocumentProvider = saveDocumentProvider;
  }

  @Override
  public CreateViewModel get() {
    return newInstance(saveDocumentProvider.get());
  }

  public static CreateViewModel_Factory create(Provider<SaveDocumentUseCase> saveDocumentProvider) {
    return new CreateViewModel_Factory(saveDocumentProvider);
  }

  public static CreateViewModel newInstance(SaveDocumentUseCase saveDocument) {
    return new CreateViewModel(saveDocument);
  }
}
