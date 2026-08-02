package com.piyush.thoughtflow.ai;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AiHttpClient_Factory implements Factory<AiHttpClient> {
  @Override
  public AiHttpClient get() {
    return newInstance();
  }

  public static AiHttpClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AiHttpClient newInstance() {
    return new AiHttpClient();
  }

  private static final class InstanceHolder {
    static final AiHttpClient_Factory INSTANCE = new AiHttpClient_Factory();
  }
}
