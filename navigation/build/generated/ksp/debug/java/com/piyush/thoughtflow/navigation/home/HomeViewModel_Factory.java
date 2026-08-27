package com.piyush.thoughtflow.navigation.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ListDocumentsUseCase> listDocumentsProvider;

  private HomeViewModel_Factory(Provider<ListDocumentsUseCase> listDocumentsProvider) {
    this.listDocumentsProvider = listDocumentsProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(listDocumentsProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<ListDocumentsUseCase> listDocumentsProvider) {
    return new HomeViewModel_Factory(listDocumentsProvider);
  }

  public static HomeViewModel newInstance(ListDocumentsUseCase listDocuments) {
    return new HomeViewModel(listDocuments);
  }
}
