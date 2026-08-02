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
public final class GeminiNanoFormatter_Factory implements Factory<GeminiNanoFormatter> {
  @Override
  public GeminiNanoFormatter get() {
    return newInstance();
  }

  public static GeminiNanoFormatter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GeminiNanoFormatter newInstance() {
    return new GeminiNanoFormatter();
  }

  private static final class InstanceHolder {
    static final GeminiNanoFormatter_Factory INSTANCE = new GeminiNanoFormatter_Factory();
  }
}
