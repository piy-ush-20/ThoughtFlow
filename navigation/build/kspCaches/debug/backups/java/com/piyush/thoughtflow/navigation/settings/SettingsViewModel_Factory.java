package com.piyush.thoughtflow.navigation.settings;

import com.piyush.thoughtflow.domain.repository.SettingsRepository;
import com.piyush.thoughtflow.domain.usecase.DetectOnDeviceAiCapabilitiesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<DetectOnDeviceAiCapabilitiesUseCase> detectOnDeviceAiCapabilitiesProvider;

  private SettingsViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<DetectOnDeviceAiCapabilitiesUseCase> detectOnDeviceAiCapabilitiesProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.detectOnDeviceAiCapabilitiesProvider = detectOnDeviceAiCapabilitiesProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsRepositoryProvider.get(), detectOnDeviceAiCapabilitiesProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<DetectOnDeviceAiCapabilitiesUseCase> detectOnDeviceAiCapabilitiesProvider) {
    return new SettingsViewModel_Factory(settingsRepositoryProvider, detectOnDeviceAiCapabilitiesProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository settingsRepository,
      DetectOnDeviceAiCapabilitiesUseCase detectOnDeviceAiCapabilities) {
    return new SettingsViewModel(settingsRepository, detectOnDeviceAiCapabilities);
  }
}
