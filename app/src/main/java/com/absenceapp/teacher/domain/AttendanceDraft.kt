package com.absenceapp.teacher.domain

data class AttendanceDraft(
    val seanceId: String,
    val absentStudentIds: Set<String>,
    val timestamp: String,
)
