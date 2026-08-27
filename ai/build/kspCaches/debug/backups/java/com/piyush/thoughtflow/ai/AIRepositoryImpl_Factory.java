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

  private final Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider;

  private AIRepositoryImpl_Factory(Provider<AdaptiveDocumentFormatter> formatterProvider,
      Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider) {
    this.formatterProvider = formatterProvider;
    this.capabilityDetectorProvider = capabilityDetectorProvider;
  }

  @Override
  public AIRepositoryImpl get() {
    return newInstance(formatterProvider.get(), capabilityDetectorProvider.get());
  }

  public static AIRepositoryImpl_Factory create(
      Provider<AdaptiveDocumentFormatter> formatterProvider,
      Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider) {
    return new AIRepositoryImpl_Factory(formatterProvider, capabilityDetectorProvider);
  }

  public static AIRepositoryImpl newInstance(AdaptiveDocumentFormatter formatter,
      OnDeviceAiCapabilityDetector capabilityDetector) {
    return new AIRepositoryImpl(formatter, capabilityDetector);
  }
}
