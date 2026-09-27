package com.absenceapp.teacher.domain

import kotlinx.serialization.Serializable

@Serializable
data class StudentRosterItem(
    val id: String,
    val num: Int = 0,
    val name: String,
    val photoUrl: String? = null,
    val isAbsent: Boolean = false,
)
