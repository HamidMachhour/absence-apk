package com.absenceapp.teacher.domain

import kotlinx.serialization.Serializable

@Serializable
data class SeanceRoster(
    val seanceId: String,
    val classeName: String,
    val subject: String,
    val salle: String,
    val students: List<StudentRosterItem>
)
