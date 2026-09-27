package com.absenceapp.teacher.data.repository

import android.content.Context
import com.absenceapp.teacher.data.remote.LaravelApiService
import com.absenceapp.teacher.domain.TeacherSession
import retrofit2.HttpException

class AuthRepository(
    private val api: LaravelApiService,
    private val context: Context,
) {
    suspend fun login(email: String, password: String): TeacherSession {
        val response = api.login(mapOf("email" to email, "password" to password))
        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val body = response.body() ?: throw IllegalStateException("Empty auth response")
        val token = body.token ?: throw IllegalStateException("Auth response missing token")
        val teacherId = body.user?.id ?: body.teacher?.id ?: body.id ?: "unknown"
        val teacherName = body.user?.name ?: body.teacher?.name ?: body.name ?: "Teacher"
        val teacherEmail = body.user?.email ?: body.teacher?.email ?: body.email ?: email

        return TeacherSession(
            teacherId = teacherId,
            teacherName = teacherName,
            token = token,
            email = teacherEmail,
        )
    }
}
