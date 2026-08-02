package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudLlmFormatter @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val okHttpClient: OkHttpClient,
    private val json: Json,
) : FormatterStrategy {

    override val id: String = ID

    override suspend fun isAvailable(): Boolean {
        val prefs = settingsRepository.preferences.first()
        if (!prefs.allowCloud) return false
        val key = settingsRepository.getCloudApiKey()?.takeIf { it.isNotBlank() }
        return key != null
    }

    override suspend fun format(text: String): FormattedDocument = withContext(Dispatchers.IO) {
        val prefs = settingsRepository.preferences.first()
        val apiKey = settingsRepository.getCloudApiKey()?.takeIf { it.isNotBlank() }
            ?: error("Cloud API key missing")

        val requestBody = ChatCompletionRequest(
            model = prefs.cloudModel,
            messages = listOf(
                ChatMessage(
                    role = "system",
                    content = SYSTEM_PROMPT,
                ),
                ChatMessage(
                    role = "user",
                    content = text,
                ),
            ),
            temperature = 0.2,
        )

        val bodyJson = json.encodeToString(ChatCompletionRequest.serializer(), requestBody)
        val request = Request.Builder()
            .url(prefs.cloudBaseUrl.trimEnd('/') + "/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .post(bodyJson.toRequestBody("application/json".toMediaType()))
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("Cloud LLM HTTP ${response.code}")
            }
            val payload = response.body?.string().orEmpty()
            val parsed = json.decodeFromString(ChatCompletionResponse.serializer(), payload)
            val content = parsed.choices.firstOrNull()?.message?.content?.trim().orEmpty()
            require(content.isNotBlank()) { "Cloud LLM returned empty content" }
            val title = content.lineSequence()
                .firstOrNull { it.startsWith("# ") }
                ?.removePrefix("# ")
                ?.trim()
                ?: content.lineSequence().first().take(48)
            FormattedDocument(
                title = title,
                bodyMarkdown = content,
                formatterId = id,
            )
        }
    }

    companion object {
        const val ID = "cloud-llm"
        private val SYSTEM_PROMPT = """
            You convert spoken transcripts into clean Markdown documents.
            Rules:
            - Start with a single H1 title
            - Use short paragraphs and bullet lists where natural
            - Do not invent facts not present in the transcript
            - Output Markdown only
        """.trimIndent()
    }
}

@Serializable
data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double = 0.2,
)

@Serializable
data class ChatMessage(
    val role: String,
    val content: String,
)

@Serializable
data class ChatCompletionResponse(
    val choices: List<Choice> = emptyList(),
)

@Serializable
data class Choice(
    val message: ChatMessagePayload? = null,
)

@Serializable
data class ChatMessagePayload(
    val role: String? = null,
    val content: String? = null,
    @SerialName("refusal") val refusal: String? = null,
)

@Singleton
class AiHttpClient @Inject constructor() {
    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}
