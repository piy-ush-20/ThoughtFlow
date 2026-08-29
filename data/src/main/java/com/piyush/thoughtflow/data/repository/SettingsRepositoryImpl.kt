package com.piyush.thoughtflow.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.piyush.thoughtflow.domain.model.AiPreferences
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "thoughtflow_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SettingsRepository {

    private val dataStore = context.settingsDataStore

    private val encryptedPrefs: SharedPreferences by lazy {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            "thoughtflow_secrets",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override val preferences: Flow<AiPreferences> = dataStore.data.map { prefs ->
        AiPreferences(
            preferOnDevice = prefs[KEY_PREFER_ON_DEVICE] ?: true,
            allowCloud = prefs[KEY_ALLOW_CLOUD] ?: false,
            cloudApiKey = null,
            cloudBaseUrl = prefs[KEY_CLOUD_BASE_URL] ?: "https://api.openai.com/v1",
            cloudModel = prefs[KEY_CLOUD_MODEL] ?: "gpt-4o-mini",
        )
    }

    override suspend fun updatePreferences(transform: (AiPreferences) -> AiPreferences) {
        dataStore.edit { prefs ->
            val current = AiPreferences(
                preferOnDevice = prefs[KEY_PREFER_ON_DEVICE] ?: true,
                allowCloud = prefs[KEY_ALLOW_CLOUD] ?: false,
                cloudBaseUrl = prefs[KEY_CLOUD_BASE_URL] ?: "https://api.openai.com/v1",
                cloudModel = prefs[KEY_CLOUD_MODEL] ?: "gpt-4o-mini",
            )
            val next = transform(current)
            prefs[KEY_PREFER_ON_DEVICE] = next.preferOnDevice
            prefs[KEY_ALLOW_CLOUD] = next.allowCloud
            prefs[KEY_CLOUD_BASE_URL] = next.cloudBaseUrl
            prefs[KEY_CLOUD_MODEL] = next.cloudModel
        }
    }

    override suspend fun setCloudApiKey(apiKey: String?) {
        if (apiKey.isNullOrBlank()) {
            encryptedPrefs.edit().remove(SECRET_API_KEY).apply()
        } else {
            encryptedPrefs.edit().putString(SECRET_API_KEY, apiKey.trim()).apply()
        }
    }

    override suspend fun getCloudApiKey(): String? =
        encryptedPrefs.getString(SECRET_API_KEY, null)

    companion object {
        private val KEY_PREFER_ON_DEVICE = booleanPreferencesKey("prefer_on_device")
        private val KEY_ALLOW_CLOUD = booleanPreferencesKey("allow_cloud")
        private val KEY_CLOUD_BASE_URL = stringPreferencesKey("cloud_base_url")
        private val KEY_CLOUD_MODEL = stringPreferencesKey("cloud_model")
        private const val SECRET_API_KEY = "cloud_api_key"
    }
}
