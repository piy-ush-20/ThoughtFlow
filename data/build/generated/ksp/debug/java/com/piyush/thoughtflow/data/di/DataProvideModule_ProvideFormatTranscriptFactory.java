package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.AIRepository;
import com.piyush.thoughtflow.domain.usecase.FormatTranscriptUseCase;
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
public final class DataProvideModule_ProvideFormatTranscriptFactory implements Factory<FormatTranscriptUseCase> {
  private final Provider<AIRepository> aiProvider;

  private DataProvideModule_ProvideFormatTranscriptFactory(Provider<AIRepository> aiProvider) {
    this.aiProvider = aiProvider;
  }

  @Override
  public FormatTranscriptUseCase get() {
    return provideFormatTranscript(aiProvider.get());
  }

  public static DataProvideModule_ProvideFormatTranscriptFactory create(
      Provider<AIRepository> aiProvider) {
    return new DataProvideModule_ProvideFormatTranscriptFactory(aiProvider);
  }

  public static FormatTranscriptUseCase provideFormatTranscript(AIRepository ai) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideFormatTranscript(ai));
  }
}
