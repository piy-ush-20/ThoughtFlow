package com.piyush.thoughtflow.data.repository;

import com.piyush.thoughtflow.data.local.DocumentDao;
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
public final class DocumentRepositoryImpl_Factory implements Factory<DocumentRepositoryImpl> {
  private final Provider<DocumentDao> daoProvider;

  private DocumentRepositoryImpl_Factory(Provider<DocumentDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public DocumentRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static DocumentRepositoryImpl_Factory create(Provider<DocumentDao> daoProvider) {
    return new DocumentRepositoryImpl_Factory(daoProvider);
  }

  public static DocumentRepositoryImpl newInstance(DocumentDao dao) {
    return new DocumentRepositoryImpl(dao);
  }
}
