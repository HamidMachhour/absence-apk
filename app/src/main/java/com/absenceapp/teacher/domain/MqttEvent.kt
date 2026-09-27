package com.absenceapp.teacher.domain

import kotlinx.serialization.Serializable

@Serializable
data class MqttEvent(
    val event: String = "",
    val seanceId: String = "",
    val salle: String = "",
    val message: String = "",
    val topic: String = "",
    val rawPayload: String = "",
)
