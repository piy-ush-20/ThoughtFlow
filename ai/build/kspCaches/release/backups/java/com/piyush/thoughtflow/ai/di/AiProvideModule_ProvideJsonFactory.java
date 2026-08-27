package com.piyush.thoughtflow.ai.di;

import com.piyush.thoughtflow.ai.AiHttpClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;

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
public final class AiProvideModule_ProvideJsonFactory implements Factory<Json> {
  private final Provider<AiHttpClient> clientProvider;

  private AiProvideModule_ProvideJsonFactory(Provider<AiHttpClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public Json get() {
    return provideJson(clientProvider.get());
  }

  public static AiProvideModule_ProvideJsonFactory create(Provider<AiHttpClient> clientProvider) {
    return new AiProvideModule_ProvideJsonFactory(clientProvider);
  }

  public static Json provideJson(AiHttpClient client) {
    return Preconditions.checkNotNullFromProvides(AiProvideModule.INSTANCE.provideJson(client));
  }
}
