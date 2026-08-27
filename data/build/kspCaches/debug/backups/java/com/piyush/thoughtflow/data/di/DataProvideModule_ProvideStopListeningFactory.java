package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.SpeechRepository;
import com.piyush.thoughtflow.domain.usecase.StopListeningUseCase;
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
public final class DataProvideModule_ProvideStopListeningFactory implements Factory<StopListeningUseCase> {
  private final Provider<SpeechRepository> speechProvider;

  private DataProvideModule_ProvideStopListeningFactory(Provider<SpeechRepository> speechProvider) {
    this.speechProvider = speechProvider;
  }

  @Override
  public StopListeningUseCase get() {
    return provideStopListening(speechProvider.get());
  }

  public static DataProvideModule_ProvideStopListeningFactory create(
      Provider<SpeechRepository> speechProvider) {
    return new DataProvideModule_ProvideStopListeningFactory(speechProvider);
  }

  public static StopListeningUseCase provideStopListening(SpeechRepository speech) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideStopListening(speech));
  }
}
