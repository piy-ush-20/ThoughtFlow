package com.piyush.thoughtflow.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AIRepositoryImpl_Factory implements Factory<AIRepositoryImpl> {
  private final Provider<AdaptiveDocumentFormatter> formatterProvider;

  private AIRepositoryImpl_Factory(Provider<AdaptiveDocumentFormatter> formatterProvider) {
    this.formatterProvider = formatterProvider;
  }

  @Override
  public AIRepositoryImpl get() {
    return newInstance(formatterProvider.get());
  }

  public static AIRepositoryImpl_Factory create(
      Provider<AdaptiveDocumentFormatter> formatterProvider) {
    return new AIRepositoryImpl_Factory(formatterProvider);
  }

  public static AIRepositoryImpl newInstance(AdaptiveDocumentFormatter formatter) {
    return new AIRepositoryImpl(formatter);
  }
}
