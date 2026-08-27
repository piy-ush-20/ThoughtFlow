package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.domain.repository.AIRepository;
import com.piyush.thoughtflow.domain.usecase.DetectOnDeviceAiCapabilitiesUseCase;
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
public final class DataProvideModule_ProvideDetectOnDeviceAiCapabilitiesFactory implements Factory<DetectOnDeviceAiCapabilitiesUseCase> {
  private final Provider<AIRepository> aiProvider;

  private DataProvideModule_ProvideDetectOnDeviceAiCapabilitiesFactory(
      Provider<AIRepository> aiProvider) {
    this.aiProvider = aiProvider;
  }

  @Override
  public DetectOnDeviceAiCapabilitiesUseCase get() {
    return provideDetectOnDeviceAiCapabilities(aiProvider.get());
  }

  public static DataProvideModule_ProvideDetectOnDeviceAiCapabilitiesFactory create(
      Provider<AIRepository> aiProvider) {
    return new DataProvideModule_ProvideDetectOnDeviceAiCapabilitiesFactory(aiProvider);
  }

  public static DetectOnDeviceAiCapabilitiesUseCase provideDetectOnDeviceAiCapabilities(
      AIRepository ai) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideDetectOnDeviceAiCapabilities(ai));
  }
}
