package com.piyush.thoughtflow.data.di;

import com.piyush.thoughtflow.data.local.DocumentDao;
import com.piyush.thoughtflow.data.local.ThoughtFlowDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataProvideModule_ProvideDocumentDaoFactory implements Factory<DocumentDao> {
  private final Provider<ThoughtFlowDatabase> dbProvider;

  private DataProvideModule_ProvideDocumentDaoFactory(Provider<ThoughtFlowDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DocumentDao get() {
    return provideDocumentDao(dbProvider.get());
  }

  public static DataProvideModule_ProvideDocumentDaoFactory create(
      Provider<ThoughtFlowDatabase> dbProvider) {
    return new DataProvideModule_ProvideDocumentDaoFactory(dbProvider);
  }

  public static DocumentDao provideDocumentDao(ThoughtFlowDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataProvideModule.INSTANCE.provideDocumentDao(db));
  }
}
