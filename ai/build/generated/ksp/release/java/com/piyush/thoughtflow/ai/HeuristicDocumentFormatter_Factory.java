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
public final class HeuristicDocumentFormatter_Factory implements Factory<HeuristicDocumentFormatter> {
  @Override
  public HeuristicDocumentFormatter get() {
    return newInstance();
  }

  public static HeuristicDocumentFormatter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HeuristicDocumentFormatter newInstance() {
    return new HeuristicDocumentFormatter();
  }

  private static final class InstanceHolder {
    static final HeuristicDocumentFormatter_Factory INSTANCE = new HeuristicDocumentFormatter_Factory();
  }
}
