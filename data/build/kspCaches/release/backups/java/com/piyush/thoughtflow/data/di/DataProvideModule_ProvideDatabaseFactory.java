package com.piyush.thoughtflow.data.di;

import android.content.Context;
import com.piyush.thoughtflow.data.local.ThoughtFlowDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataProvideModule_ProvideDatabaseFactory implements Factory<ThoughtFlowDatabase> {
  private final Provider<Context> contextProvider;

  private DataProvideModule_ProvideDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ThoughtFlowDatabase get() {
    return provideDatabase(contextProvider.get());
  }

  public static DataProvideModule_ProvideDatabaseFactory create(Provider<Context> contextProvider) {
    return new DataProvideModule_ProvideDatabaseFactory(contextProvider);
  }

  public static ThoughtFlowDatabase provideDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideDatabase(context));
  }
}
