package com.piyush.thoughtflow.export;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ExportWorker_AssistedFactory_Impl implements ExportWorker_AssistedFactory {
  private final ExportWorker_Factory delegateFactory;

  ExportWorker_AssistedFactory_Impl(ExportWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ExportWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<ExportWorker_AssistedFactory> create(
      ExportWorker_Factory delegateFactory) {
    return InstanceFactory.create(new ExportWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<ExportWorker_AssistedFactory> createFactoryProvider(
      ExportWorker_Factory delegateFactory) {
    return InstanceFactory.create(new ExportWorker_AssistedFactory_Impl(delegateFactory));
  }
}
