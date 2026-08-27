package com.piyush.thoughtflow.speech;

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
public final class SpeechRepositoryImpl_Factory implements Factory<SpeechRepositoryImpl> {
  private final Provider<SpeechEngine> engineProvider;

  private SpeechRepositoryImpl_Factory(Provider<SpeechEngine> engineProvider) {
    this.engineProvider = engineProvider;
  }

  @Override
  public SpeechRepositoryImpl get() {
    return newInstance(engineProvider.get());
  }

  public static SpeechRepositoryImpl_Factory create(Provider<SpeechEngine> engineProvider) {
    return new SpeechRepositoryImpl_Factory(engineProvider);
  }

  public static SpeechRepositoryImpl newInstance(SpeechEngine engine) {
    return new SpeechRepositoryImpl(engine);
  }
}
