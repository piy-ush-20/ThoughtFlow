package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.AiPreferences
import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptiveDocumentFormatterTest {

    @Test
    fun fallsBackToHeuristicWhenNanoUnavailable() = runBlocking {
        val heuristic = HeuristicDocumentFormatter()
        val nano = GeminiNanoFormatter()
        val settings = FakeSettings(AiPreferences(preferOnDevice = true, allowCloud = false))
        val adaptive = AdaptiveDocumentFormatter(
            geminiNano = nano,
            heuristic = heuristic,
            cloud = CloudLlmFormatter(settings, AiHttpClient().client, AiHttpClient().json),
            settingsRepository = settings,
        )
        val result = adaptive.format("Project update. We finished the pipeline. Todo write docs.")
        assertEquals(HeuristicDocumentFormatter.ID, result.formatterId)
    }

    private class FakeSettings(
        private val prefs: AiPreferences,
    ) : SettingsRepository {
        override val preferences: Flow<AiPreferences> = flowOf(prefs)
        override suspend fun updatePreferences(transform: (AiPreferences) -> AiPreferences) = Unit
        override suspend fun setCloudApiKey(apiKey: String?) = Unit
        override suspend fun getCloudApiKey(): String? = null
    }
}
