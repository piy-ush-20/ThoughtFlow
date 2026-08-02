package com.piyush.thoughtflow.export;

import android.content.Context;
import com.piyush.thoughtflow.domain.repository.DocumentRepository;
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
public final class ExportRepositoryImpl_Factory implements Factory<ExportRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<DocumentRepository> documentRepositoryProvider;

  private final Provider<ExportWorkScheduler> workSchedulerProvider;

  private ExportRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<DocumentRepository> documentRepositoryProvider,
      Provider<ExportWorkScheduler> workSchedulerProvider) {
    this.contextProvider = contextProvider;
    this.documentRepositoryProvider = documentRepositoryProvider;
    this.workSchedulerProvider = workSchedulerProvider;
  }

  @Override
  public ExportRepositoryImpl get() {
    return newInstance(contextProvider.get(), documentRepositoryProvider.get(), workSchedulerProvider.get());
  }

  public static ExportRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<DocumentRepository> documentRepositoryProvider,
      Provider<ExportWorkScheduler> workSchedulerProvider) {
    return new ExportRepositoryImpl_Factory(contextProvider, documentRepositoryProvider, workSchedulerProvider);
  }

  public static ExportRepositoryImpl newInstance(Context context,
      DocumentRepository documentRepository, ExportWorkScheduler workScheduler) {
    return new ExportRepositoryImpl(context, documentRepository, workScheduler);
  }
}
