package com.absenceapp.teacher.domain

import kotlinx.serialization.Serializable

@Serializable
data class TeacherSession(
    val teacherId: String,
    val teacherName: String,
    val token: String,
    val email: String? = null,
)
