package com.piyush.thoughtflow.navigation.home;

import com.piyush.thoughtflow.processing.VoiceDocumentPipeline;
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
public final class RecordingViewModel_Factory implements Factory<RecordingViewModel> {
  private final Provider<VoiceDocumentPipeline> pipelineProvider;

  private RecordingViewModel_Factory(Provider<VoiceDocumentPipeline> pipelineProvider) {
    this.pipelineProvider = pipelineProvider;
  }

  @Override
  public RecordingViewModel get() {
    return newInstance(pipelineProvider.get());
  }

  public static RecordingViewModel_Factory create(
      Provider<VoiceDocumentPipeline> pipelineProvider) {
    return new RecordingViewModel_Factory(pipelineProvider);
  }

  public static RecordingViewModel newInstance(VoiceDocumentPipeline pipeline) {
    return new RecordingViewModel(pipeline);
  }
}
