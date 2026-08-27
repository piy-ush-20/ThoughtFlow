package com.piyush.thoughtflow.ai;

import com.piyush.thoughtflow.domain.repository.SettingsRepository;
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
public final class AdaptiveDocumentFormatter_Factory implements Factory<AdaptiveDocumentFormatter> {
  private final Provider<GeminiNanoFormatter> geminiNanoProvider;

  private final Provider<HeuristicDocumentFormatter> heuristicProvider;

  private final Provider<CloudLlmFormatter> cloudProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private AdaptiveDocumentFormatter_Factory(Provider<GeminiNanoFormatter> geminiNanoProvider,
      Provider<HeuristicDocumentFormatter> heuristicProvider,
      Provider<CloudLlmFormatter> cloudProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.geminiNanoProvider = geminiNanoProvider;
    this.heuristicProvider = heuristicProvider;
    this.cloudProvider = cloudProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public AdaptiveDocumentFormatter get() {
    return newInstance(geminiNanoProvider.get(), heuristicProvider.get(), cloudProvider.get(), settingsRepositoryProvider.get());
  }

  public static AdaptiveDocumentFormatter_Factory create(
      Provider<GeminiNanoFormatter> geminiNanoProvider,
      Provider<HeuristicDocumentFormatter> heuristicProvider,
      Provider<CloudLlmFormatter> cloudProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new AdaptiveDocumentFormatter_Factory(geminiNanoProvider, heuristicProvider, cloudProvider, settingsRepositoryProvider);
  }

  public static AdaptiveDocumentFormatter newInstance(GeminiNanoFormatter geminiNano,
      HeuristicDocumentFormatter heuristic, CloudLlmFormatter cloud,
      SettingsRepository settingsRepository) {
    return new AdaptiveDocumentFormatter(geminiNano, heuristic, cloud, settingsRepository);
  }
}
