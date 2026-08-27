package com.piyush.thoughtflow.ai;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class OnDeviceAiCapabilityDetector_Factory implements Factory<OnDeviceAiCapabilityDetector> {
  private final Provider<Context> contextProvider;

  private OnDeviceAiCapabilityDetector_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OnDeviceAiCapabilityDetector get() {
    return newInstance(contextProvider.get());
  }

  public static OnDeviceAiCapabilityDetector_Factory create(Provider<Context> contextProvider) {
    return new OnDeviceAiCapabilityDetector_Factory(contextProvider);
  }

  public static OnDeviceAiCapabilityDetector newInstance(Context context) {
    return new OnDeviceAiCapabilityDetector(context);
  }
}
