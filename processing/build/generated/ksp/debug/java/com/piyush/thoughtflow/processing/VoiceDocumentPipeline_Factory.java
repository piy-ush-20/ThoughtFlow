package com.piyush.thoughtflow.processing;

import com.piyush.thoughtflow.domain.repository.SpeechRepository;
import com.piyush.thoughtflow.domain.usecase.FormatTranscriptUseCase;
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase;
import com.piyush.thoughtflow.domain.usecase.StartListeningUseCase;
import com.piyush.thoughtflow.domain.usecase.StopListeningUseCase;
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
public final class VoiceDocumentPipeline_Factory implements Factory<VoiceDocumentPipeline> {
  private final Provider<SpeechRepository> speechRepositoryProvider;

  private final Provider<StartListeningUseCase> startListeningProvider;

  private final Provider<StopListeningUseCase> stopListeningProvider;

  private final Provider<FormatTranscriptUseCase> formatTranscriptProvider;

  private final Provider<SaveDocumentUseCase> saveDocumentProvider;

  private VoiceDocumentPipeline_Factory(Provider<SpeechRepository> speechRepositoryProvider,
      Provider<StartListeningUseCase> startListeningProvider,
      Provider<StopListeningUseCase> stopListeningProvider,
      Provider<FormatTranscriptUseCase> formatTranscriptProvider,
      Provider<SaveDocumentUseCase> saveDocumentProvider) {
    this.speechRepositoryProvider = speechRepositoryProvider;
    this.startListeningProvider = startListeningProvider;
    this.stopListeningProvider = stopListeningProvider;
    this.formatTranscriptProvider = formatTranscriptProvider;
    this.saveDocumentProvider = saveDocumentProvider;
  }

  @Override
  public VoiceDocumentPipeline get() {
    return newInstance(speechRepositoryProvider.get(), startListeningProvider.get(), stopListeningProvider.get(), formatTranscriptProvider.get(), saveDocumentProvider.get());
  }

  public static VoiceDocumentPipeline_Factory create(
      Provider<SpeechRepository> speechRepositoryProvider,
      Provider<StartListeningUseCase> startListeningProvider,
      Provider<StopListeningUseCase> stopListeningProvider,
      Provider<FormatTranscriptUseCase> formatTranscriptProvider,
      Provider<SaveDocumentUseCase> saveDocumentProvider) {
    return new VoiceDocumentPipeline_Factory(speechRepositoryProvider, startListeningProvider, stopListeningProvider, formatTranscriptProvider, saveDocumentProvider);
  }

  public static VoiceDocumentPipeline newInstance(SpeechRepository speechRepository,
      StartListeningUseCase startListening, StopListeningUseCase stopListening,
      FormatTranscriptUseCase formatTranscript, SaveDocumentUseCase saveDocument) {
    return new VoiceDocumentPipeline(speechRepository, startListening, stopListening, formatTranscript, saveDocument);
  }
}
