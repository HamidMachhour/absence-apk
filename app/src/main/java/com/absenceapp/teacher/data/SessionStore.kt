package com.absenceapp.teacher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.absenceapp.teacher.domain.AppLanguage
import com.absenceapp.teacher.domain.MqttConfig
import com.absenceapp.teacher.domain.TeacherSession
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "absence_app_prefs")

class SessionStore(private val context: Context) {
    private object Keys {
        val token = stringPreferencesKey("auth_token")
        val teacherId = stringPreferencesKey("teacher_id")
        val teacherName = stringPreferencesKey("teacher_name")
        val email = stringPreferencesKey("teacher_email")
        val baseUrl = stringPreferencesKey("laravel_base_url")
        val language = stringPreferencesKey("app_language")
        val mqttHost = stringPreferencesKey("mqtt_host")
        val mqttPort = stringPreferencesKey("mqtt_port")
        val mqttUsername = stringPreferencesKey("mqtt_username")
        val mqttPassword = stringPreferencesKey("mqtt_password")
    }

    suspend fun saveLoginSession(session: TeacherSession, baseUrl: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.token] = session.token
            prefs[Keys.teacherId] = session.teacherId
            prefs[Keys.teacherName] = session.teacherName
            prefs[Keys.email] = session.email ?: ""
            prefs[Keys.baseUrl] = baseUrl.trimEnd('/')
        }
    }

    suspend fun saveSettings(
        baseUrl: String,
        mqttHost: String,
        mqttPort: String,
        mqttUsername: String,
        mqttPassword: String,
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.baseUrl] = baseUrl.trimEnd('/')
            prefs[Keys.mqttHost] = mqttHost
            prefs[Keys.mqttPort] = mqttPort
            prefs[Keys.mqttUsername] = mqttUsername
            prefs[Keys.mqttPassword] = mqttPassword
        }
    }

    suspend fun saveLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[Keys.language] = language.code
        }
    }

    suspend fun getLanguage(): AppLanguage =
        context.dataStore.data.map { prefs ->
            AppLanguage.fromCode(prefs[Keys.language] ?: "en")
        }.firstOrNull() ?: AppLanguage.EN

    suspend fun getBaseUrl(): String =
        context.dataStore.data.map { it[Keys.baseUrl] ?: "" }.firstOrNull() ?: ""

    suspend fun getMqttConfig(): MqttConfig =
        context.dataStore.data.map { prefs ->
            MqttConfig(
                host = prefs[Keys.mqttHost] ?: "",
                port = prefs[Keys.mqttPort]?.toIntOrNull() ?: 1883,
                username = prefs[Keys.mqttUsername] ?: "",
                password = prefs[Keys.mqttPassword] ?: "",
            )
        }.firstOrNull() ?: MqttConfig()

    suspend fun currentSession(): TeacherSession? =
        context.dataStore.data.map { prefs ->
            val token = prefs[Keys.token] ?: return@map null
            val teacherId = prefs[Keys.teacherId] ?: return@map null
            val teacherName = prefs[Keys.teacherName] ?: return@map null
            TeacherSession(
                teacherId = teacherId,
                teacherName = teacherName,
                token = token,
                email = prefs[Keys.email].takeUnless { it.isNullOrBlank() }
            )
        }.firstOrNull()

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.token)
            prefs.remove(Keys.teacherId)
            prefs.remove(Keys.teacherName)
            prefs.remove(Keys.email)
        }
    }
}
