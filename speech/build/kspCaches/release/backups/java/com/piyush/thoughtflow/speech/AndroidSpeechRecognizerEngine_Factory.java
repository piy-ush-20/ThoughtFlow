package com.piyush.thoughtflow.speech;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AndroidSpeechRecognizerEngine_Factory implements Factory<AndroidSpeechRecognizerEngine> {
  private final Provider<Context> appContextProvider;

  private AndroidSpeechRecognizerEngine_Factory(Provider<Context> appContextProvider) {
    this.appContextProvider = appContextProvider;
  }

  @Override
  public AndroidSpeechRecognizerEngine get() {
    return newInstance(appContextProvider.get());
  }

  public static AndroidSpeechRecognizerEngine_Factory create(Provider<Context> appContextProvider) {
    return new AndroidSpeechRecognizerEngine_Factory(appContextProvider);
  }

  public static AndroidSpeechRecognizerEngine newInstance(Context appContext) {
    return new AndroidSpeechRecognizerEngine(appContext);
  }
}
