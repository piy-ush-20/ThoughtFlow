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
public final class GeminiNanoFormatter_Factory implements Factory<GeminiNanoFormatter> {
  private final Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider;

  private GeminiNanoFormatter_Factory(
      Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider) {
    this.capabilityDetectorProvider = capabilityDetectorProvider;
  }

  @Override
  public GeminiNanoFormatter get() {
    return newInstance(capabilityDetectorProvider.get());
  }

  public static GeminiNanoFormatter_Factory create(
      Provider<OnDeviceAiCapabilityDetector> capabilityDetectorProvider) {
    return new GeminiNanoFormatter_Factory(capabilityDetectorProvider);
  }

  public static GeminiNanoFormatter newInstance(OnDeviceAiCapabilityDetector capabilityDetector) {
    return new GeminiNanoFormatter(capabilityDetector);
  }
}
