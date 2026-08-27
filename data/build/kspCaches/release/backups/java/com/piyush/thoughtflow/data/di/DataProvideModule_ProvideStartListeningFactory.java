package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.SpeechRepository;
import com.piyush.thoughtflow.domain.usecase.StartListeningUseCase;
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
public final class DataProvideModule_ProvideStartListeningFactory implements Factory<StartListeningUseCase> {
  private final Provider<SpeechRepository> speechProvider;

  private DataProvideModule_ProvideStartListeningFactory(
      Provider<SpeechRepository> speechProvider) {
    this.speechProvider = speechProvider;
  }

  @Override
  public StartListeningUseCase get() {
    return provideStartListening(speechProvider.get());
  }

  public static DataProvideModule_ProvideStartListeningFactory create(
      Provider<SpeechRepository> speechProvider) {
    return new DataProvideModule_ProvideStartListeningFactory(speechProvider);
  }

  public static StartListeningUseCase provideStartListening(SpeechRepository speech) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideStartListening(speech));
  }
}
