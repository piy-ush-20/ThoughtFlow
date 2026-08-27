# ThoughtFlow

Privacy-first, offline-capable voice-to-document Android app.

Speak naturally → continuous speech-to-text → AI structuring → editable Markdown document → local save / export.

Raw audio is **never** permanently stored.

## Architecture

Clean Architecture with multi-module Gradle boundaries:

```text
app → navigation → (ui, editor, export, processing)
processing → speech, ai, domain
data → domain (Room + DataStore)
ai → DocumentFormatter (Adaptive: Gemini Nano → Heuristic → optional Cloud)
```

UI never depends on a specific AI vendor. Only:

```kotlin
DocumentFormatter.format(text)
```

is exposed.

### Modules

| Module | Responsibility |
|--------|----------------|
| `:domain` | Models, use cases, repository interfaces, voice session state machine |
| `:data` | Room documents, settings, encrypted API key storage |
| `:audio` | PCM `AudioCapture` for future engines (not used with platform STT) |
| `:speech` | `SpeechEngine` + Android `SpeechRecognizer` (partials + continuous) |
| `:ai` | Adaptive `DocumentFormatter` (Nano / Heuristic / Cloud) |
| `:processing` | Voice → document pipeline orchestration |
| `:editor` | Document editor + save/export actions |
| `:export` | Markdown/TXT writers, Share, WorkManager export jobs |
| `:ui` | Celestial dark theme (Sora), glass/gradient components, bottom nav |
| `:navigation` | Bottom-nav host + Home/Create/Voice/Documents/Templates/Profile/Store/Processing |
| `:app` | `ThoughtFlowApp`, `MainActivity`, Hilt, packaging |

### Voice session state machine

`Idle → Listening → Transcribing → Formatting → Editing → Saved`

## Privacy

1. Platform STT owns the microphone (no parallel `AudioRecord` contention).
2. PCM / temp WAV paths (when used by future engines) write to `cacheDir` and delete in `finally`.
3. Only structured documents are persisted in Room.
4. Cloud LLM is **off by default**; API keys use EncryptedSharedPreferences.

## Build

Requirements: JDK 17+, Android SDK 36, Gradle wrapper included.

```bash
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
./gradlew :app:assembleRelease
```

### Release signing

Set environment variables (do not commit the keystore):

```bash
export KEYSTORE_FILE=/absolute/path/to/upload.jks
export KEYSTORE_PASSWORD=...
export KEY_ALIAS=...
export KEY_PASSWORD=...
./gradlew :app:bundleRelease
```

Without these, release builds still assemble (unsigned / debug-fallback for local CI).

## Play Store checklist

- [ ] Create upload keystore and store securely
- [ ] `bundleRelease` AAB uploaded to Play Console
- [ ] Short description / full description (privacy-first voice documents)
- [ ] Feature graphic + screenshots (phone)
- [ ] Data safety: Microphone — used for speech recognition; audio not stored
- [ ] Privacy policy URL stating no permanent audio retention
- [ ] Content rating questionnaire

## Local run (Android Studio)

1. Open the project root.
2. Sync Gradle.
3. Run `:app` on a device/emulator with Google speech services (for STT).
4. Hold the mic → speak → release → edit the generated document.

## Settings / Profile

- **On-device AI** — runtime detection of AICore, Gemini Nano feature status, GenAI SDK linkage, and on-device speech
- **Prefer on-device** — try Gemini Nano, then always-available heuristic formatter
- **Allow cloud LLM** — optional OpenAI-compatible HTTP fallback

## UI

Celestial dark theme (`#0D0D12`) with purple/blue brand gradients, Sora typography, glass cards, and a 5-tab bottom bar (Home, Documents, Create, Templates, Profile).

## License

Private / all rights reserved unless otherwise stated.
