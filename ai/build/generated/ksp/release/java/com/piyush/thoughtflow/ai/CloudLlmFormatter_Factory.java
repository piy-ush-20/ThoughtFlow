package com.piyush.thoughtflow.ai;

import com.piyush.thoughtflow.domain.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.serialization.json.Json;
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
public final class CloudLlmFormatter_Factory implements Factory<CloudLlmFormatter> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<Json> jsonProvider;

  private CloudLlmFormatter_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<OkHttpClient> okHttpClientProvider, Provider<Json> jsonProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.okHttpClientProvider = okHttpClientProvider;
    this.jsonProvider = jsonProvider;
  }

  @Override
  public CloudLlmFormatter get() {
    return newInstance(settingsRepositoryProvider.get(), okHttpClientProvider.get(), jsonProvider.get());
  }

  public static CloudLlmFormatter_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<OkHttpClient> okHttpClientProvider, Provider<Json> jsonProvider) {
    return new CloudLlmFormatter_Factory(settingsRepositoryProvider, okHttpClientProvider, jsonProvider);
  }

  public static CloudLlmFormatter newInstance(SettingsRepository settingsRepository,
      OkHttpClient okHttpClient, Json json) {
    return new CloudLlmFormatter(settingsRepository, okHttpClient, json);
  }
}
