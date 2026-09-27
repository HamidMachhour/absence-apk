package com.absenceapp.teacher.domain

enum class AppLanguage(val code: String) {
    EN("en"),
    AR("ar");

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code } ?: EN
    }
}
