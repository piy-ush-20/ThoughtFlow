package com.piyush.thoughtflow.export;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.piyush.thoughtflow.domain.repository.DocumentRepository;
import dagger.internal.DaggerGenerated;
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
public final class ExportWorker_Factory {
  private final Provider<DocumentRepository> documentRepositoryProvider;

  private final Provider<ExportRepositoryImpl> exportRepositoryProvider;

  private ExportWorker_Factory(Provider<DocumentRepository> documentRepositoryProvider,
      Provider<ExportRepositoryImpl> exportRepositoryProvider) {
    this.documentRepositoryProvider = documentRepositoryProvider;
    this.exportRepositoryProvider = exportRepositoryProvider;
  }

  public ExportWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, documentRepositoryProvider.get(), exportRepositoryProvider.get());
  }

  public static ExportWorker_Factory create(Provider<DocumentRepository> documentRepositoryProvider,
      Provider<ExportRepositoryImpl> exportRepositoryProvider) {
    return new ExportWorker_Factory(documentRepositoryProvider, exportRepositoryProvider);
  }

  public static ExportWorker newInstance(Context context, WorkerParameters params,
      DocumentRepository documentRepository, ExportRepositoryImpl exportRepository) {
    return new ExportWorker(context, params, documentRepository, exportRepository);
  }
}
