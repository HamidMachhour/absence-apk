package com.absenceapp.teacher.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absenceapp.teacher.AppContainer
import com.absenceapp.teacher.domain.AppLanguage
import com.absenceapp.teacher.domain.MqttConfig
import com.absenceapp.teacher.domain.MqttEvent
import com.absenceapp.teacher.domain.SeanceRoster
import com.absenceapp.teacher.domain.StudentRosterItem
import com.absenceapp.teacher.domain.TeacherSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SeanceOption(
    val id: String,
    val label: String,
)

class TeacherViewModel(private val container: AppContainer) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState

    init {
        viewModelScope.launch {
            val session = container.sessionStore.currentSession()
            val savedLanguage = container.sessionStore.getLanguage()
            _uiState.value = _uiState.value.copy(
                session = session,
                isLoggedIn = session != null,
                baseUrl = container.sessionStore.getBaseUrl(),
                language = savedLanguage,
                mqttConfig = container.sessionStore.getMqttConfig(),
            )

            if (session != null) {
                loadTeacherSeances()
            }

            container.db.rosterDao().observeAllRosters().collect { rosters ->
                val options = rosters.map { room ->
                    val label = when {
                        room.classeName.isNotBlank() && room.subject.isNotBlank() -> "${room.classeName} • ${room.subject}"
                        room.classeName.isNotBlank() -> room.classeName
                        room.subject.isNotBlank() -> room.subject
                        else -> "Saved seance ${room.seanceId.take(8)}"
                    }
                    SeanceOption(id = room.seanceId, label = label)
                }

                val mergedOptions = if (_uiState.value.availableSeanceOptions.isNotEmpty()) {
                    _uiState.value.availableSeanceOptions
                } else {
                    options
                }

                _uiState.value = _uiState.value.copy(availableSeanceOptions = mergedOptions)
                if (_uiState.value.seanceId.isBlank() && mergedOptions.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(seanceId = mergedOptions.first().id)
                }
            }
        }
    }

    fun updateBaseUrl(value: String) {
        _uiState.value = _uiState.value.copy(baseUrl = value)
    }

    fun saveBaseUrl() {
        val baseUrl = _uiState.value.baseUrl.trim()
        if (baseUrl.isBlank()) return

        viewModelScope.launch {
            container.sessionStore.saveBaseUrl(baseUrl)
            container.updateApiBaseUrl(baseUrl)
            _uiState.value = _uiState.value.copy(
                baseUrl = baseUrl,
                settingsMessage = "Base URL saved",
            )

            if (_uiState.value.session != null) {
                loadTeacherSeances()
            }
        }
    }

    fun updateMqttConfig(config: MqttConfig) {
        _uiState.value = _uiState.value.copy(mqttConfig = config)
    }

    fun updateLoginForm(email: String, password: String) {
        _uiState.value = _uiState.value.copy(email = email, password = password)
    }

    fun updateSeanceId(value: String) {
        _uiState.value = _uiState.value.copy(seanceId = value)
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.value = _uiState.value.copy(language = language)
        viewModelScope.launch { container.sessionStore.saveLanguage(language) }
    }

    fun selectSeance(seanceId: String) {
        _uiState.value = _uiState.value.copy(seanceId = seanceId)
    }

    fun loadTeacherSeances() {
        val session = _uiState.value.session ?: return
        viewModelScope.launch {
            try {
                val response = container.laravelApiClient.service.getTeacherSeances("Bearer ${session.token}")
                if (!response.isSuccessful) {
                    throw IllegalStateException("Unable to load teacher seances")
                }

                val payload = response.body() ?: return@launch
                val options = payload.seances.map { s ->
                    val timeRange = s.time_range_short?.takeIf { it.isNotBlank() }
                        ?: s.time_range?.takeIf { it.isNotBlank() }
                        ?: listOfNotNull(s.de, s.a).joinToString(" - ").ifBlank { s.id }

                    val label = buildString {
                        append(timeRange)
                        if (!s.classe_name.isNullOrBlank()) append(" • ${s.classe_name}")
                        if (!s.subject.isNullOrBlank()) append(" • ${s.subject}")
                    }

                    SeanceOption(id = s.id, label = label)
                }

                if (options.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        availableSeanceOptions = options,
                        seanceId = options.first().id,
                    )
                }
            } catch (_: Exception) {
                // Keep previously cached options if the API is temporarily unavailable.
            }
        }
    }

    fun login(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val session = container.authRepository.login(
                    _uiState.value.email.trim(),
                    _uiState.value.password,
                )
                container.sessionStore.saveLoginSession(session, _uiState.value.baseUrl)
                container.sessionStore.saveSettings(
                    baseUrl = _uiState.value.baseUrl,
                    mqttHost = _uiState.value.mqttConfig.host,
                    mqttPort = _uiState.value.mqttConfig.port.toString(),
                    mqttUsername = _uiState.value.mqttConfig.username,
                    mqttPassword = _uiState.value.mqttConfig.password,
                )
                container.updateApiBaseUrl(_uiState.value.baseUrl)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    session = session,
                    isLoggedIn = true,
                    errorMessage = null,
                )
                loadTeacherSeances()
                connectMqtt(session)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message ?: "Login failed")
                onError(_uiState.value.errorMessage ?: "Login failed")
            }
        }
    }

    fun loadRoster() {
        val session = _uiState.value.session ?: return
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, rosterError = null)
                val roster = container.rosterRepository.fetchRoster(_uiState.value.seanceId, session.token)
                _uiState.value = _uiState.value.copy(
                    roster = roster,
                    isLoading = false,
                    rosterError = null,
                    attendanceDraft = roster.students.associate { it.id to it.isAbsent }.toMutableMap(),
                )
            } catch (e: Exception) {
                val friendlyError = when {
                    e.message?.contains("Unexpected JSON token") == true || e.message?.contains("<!DOCTYPE html>") == true ->
                        "API returned HTML instead of JSON. Check the roster route and auth token."
                    else -> e.message ?: "Failed to load roster"
                }
                _uiState.value = _uiState.value.copy(isLoading = false, rosterError = friendlyError)
            }
        }
    }

    fun openLastRoster() {
        viewModelScope.launch {
            val roster = container.rosterRepository.getLastSavedRoster()
            if (roster != null) {
                _uiState.value = _uiState.value.copy(
                    roster = roster,
                    seanceId = roster.seanceId,
                    attendanceDraft = roster.students.associate { it.id to it.isAbsent }.toMutableMap(),
                )
            }
        }
    }

    fun toggleAbsent(studentId: String) {
        val current = _uiState.value.attendanceDraft.toMutableMap()
        current[studentId] = !(current[studentId] ?: false)
        _uiState.value = _uiState.value.copy(attendanceDraft = current)
    }

    fun resetAttendance() {
        val roster = _uiState.value.roster ?: return
        _uiState.value = _uiState.value.copy(
            attendanceDraft = roster.students.associate { it.id to false }.toMutableMap(),
        )
    }

    /** Bulk action: marks every student in the current roster as absent by default. */
    fun markAllAbsent() {
        val roster = _uiState.value.roster ?: return
        _uiState.value = _uiState.value.copy(
            attendanceDraft = roster.students.associate { it.id to true }.toMutableMap(),
        )
    }

    /**
     * Matches a scanned student code (tried against the student id, then the student number)
     * and marks that student present. Returns the matched student's display name, or null if
     * no student in the current roster matches the scanned code.
     */
    fun markPresentByScannedCode(code: String): String? {
        val roster = _uiState.value.roster ?: return null
        val trimmed = code.trim()
        val studentId = try {
            val jsonObject = org.json.JSONObject(trimmed)
            jsonObject.getString("student_id")
        } catch (e: Exception) {
            null 
        }
        val student = roster.students.firstOrNull { it.userId == studentId ||  it.id == studentId } ?: return null
        val current = _uiState.value.attendanceDraft.toMutableMap()
        current[student.id] = false
        _uiState.value = _uiState.value.copy(attendanceDraft = current)
        return student.name
    }

    fun submitAttendance() {
        val session = _uiState.value.session ?: return
        val roster = _uiState.value.roster ?: return
        viewModelScope.launch {
            try {
                val absentIds = roster.students.filter { _uiState.value.attendanceDraft[it.id] == true }.map { it.id }.toSet()
                _uiState.value = _uiState.value.copy(isSubmitting = true, submitError = null)
                val success = container.attendanceRepository.submitAttendance(
                    session,
                    roster.seanceId,
                    absentIds,
                    _uiState.value.baseUrl,
                )
                if (success) {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, pendingSync = false)
                } else {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, pendingSync = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSubmitting = false, submitError = e.message ?: "Submit failed", pendingSync = true)
            }
        }
    }

    fun syncPendingIfNeeded() {
        val session = _uiState.value.session ?: return
        viewModelScope.launch {
            val synced = container.attendanceRepository.syncPending(session, _uiState.value.baseUrl)
            _uiState.value = _uiState.value.copy(pendingSync = !synced)
        }
    }

    fun connectMqtt(session: TeacherSession) {
        viewModelScope.launch {
            try {
                val cfg = _uiState.value.mqttConfig
                container.mqttManager.connect(cfg, session.teacherId)
                container.mqttManager.subscribeToTeacherAlerts(session.teacherId)
                container.mqttManager.addListener { event ->
                    _uiState.value = _uiState.value.copy(
                        lastMqttMessage = event.message,
                        mqttStatus = if (event.event == "seance_starting") "Seance starting" else "Connected",
                    )
                }
                _uiState.value = _uiState.value.copy(
                    mqttStatus = "Connected",
                    mqttConnected = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(mqttStatus = "Disconnected", mqttConnected = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            container.sessionStore.clearSession()
            _uiState.value = AppUiState()
        }
    }
}

data class AppUiState(
    val session: TeacherSession? = null,
    val email: String = "",
    val password: String = "",
    val language: AppLanguage = AppLanguage.EN,
    val baseUrl: String = "http://10.0.0.11:8282",
    val seanceId: String = "",
    val availableSeanceOptions: List<SeanceOption> = emptyList(),
    val roster: SeanceRoster? = null,
    val attendanceDraft: MutableMap<String, Boolean> = mutableMapOf(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val pendingSync: Boolean = false,
    val errorMessage: String? = null,
    val rosterError: String? = null,
    val submitError: String? = null,
    val isLoggedIn: Boolean = false,
    val mqttConfig: MqttConfig = MqttConfig(),
    val mqttStatus: String = "Disconnected",
    val mqttConnected: Boolean = false,
    val lastMqttMessage: String = "",
    val settingsMessage: String? = null,
)
