package com.piyush.thoughtflow.ai.di;

import com.piyush.thoughtflow.ai.AiHttpClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class AiProvideModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<AiHttpClient> clientProvider;

  private AiProvideModule_ProvideOkHttpFactory(Provider<AiHttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(clientProvider.get());
  }

  public static AiProvideModule_ProvideOkHttpFactory create(Provider<AiHttpClient> clientProvider) {
    return new AiProvideModule_ProvideOkHttpFactory(clientProvider);
  }

  public static OkHttpClient provideOkHttp(AiHttpClient client) {
    return Preconditions.checkNotNullFromProvides(AiProvideModule.INSTANCE.provideOkHttp(client));
  }
}
