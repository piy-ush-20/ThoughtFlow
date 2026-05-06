package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.AiPreferences
import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provider-independent entry point. UI only calls [format].
 */
@Singleton
class AdaptiveDocumentFormatter @Inject constructor(
    private val geminiNano: GeminiNanoFormatter,
    private val heuristic: HeuristicDocumentFormatter,
    private val cloud: CloudLlmFormatter,
    private val settingsRepository: SettingsRepository,
) : DocumentFormatter {

    override val id: String = "adaptive"

    override suspend fun isAvailable(): Boolean = true

    override suspend fun format(text: String): FormattedDocument {
        val prefs = settingsRepository.preferences.first()
        val strategies = orderedStrategies(prefs)
        var lastError: Throwable? = null
        for (strategy in strategies) {
            try {
                if (!strategy.isAvailable()) continue
                return strategy.format(text)
            } catch (t: Throwable) {
                lastError = t
            }
        }
        // Absolute last resort — heuristic must always work
        return try {
            heuristic.format(text)
        } catch (t: Throwable) {
            throw lastError ?: t
        }
    }

    private fun orderedStrategies(prefs: AiPreferences): List<FormatterStrategy> {
        val list = mutableListOf<FormatterStrategy>()
        if (prefs.preferOnDevice) {
            list += geminiNano
            list += heuristic
            if (prefs.allowCloud) list += cloud
        } else {
            if (prefs.allowCloud) list += cloud
            list += geminiNano
            list += heuristic
        }
        // Ensure heuristic is always present once
        if (list.none { it.id == HeuristicDocumentFormatter.ID }) {
            list += heuristic
        }
        return list
    }
}
