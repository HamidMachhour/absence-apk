package com.absenceapp.teacher.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.absenceapp.teacher.data.local.AppDatabase
import com.absenceapp.teacher.data.local.RoomPendingAttendance
import com.absenceapp.teacher.data.remote.AttendanceRequestDto
import com.absenceapp.teacher.domain.TeacherSession
import com.absenceapp.teacher.network.LaravelApiClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceRepository(
    private val context: Context,
    private val db: AppDatabase,
) {
    suspend fun submitAttendance(
        session: TeacherSession,
        seanceId: String,
        absentIds: Set<String>,
        baseUrl: String,
    ): Boolean {
        val ids = absentIds.toList().sorted()
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        if (!isOnline()) {
            val pending = RoomPendingAttendance(
                id = "${seanceId}-${System.currentTimeMillis()}",
                seanceId = seanceId,
                absentStudentIds = ids.joinToString(","),
                timestamp = timestamp,
                lastStatus = "PENDING",
            )
            db.pendingAttendanceDao().save(pending)
            return false
        }

        val api = LaravelApiClient(baseUrl).service
        val response = api.submitAttendance(
            seance = seanceId,
            authorization = "Bearer ${session.token}",
            body = AttendanceRequestDto(absent_student_ids = ids, timestamp = timestamp),
        )

        return response.isSuccessful
    }

    suspend fun queuePendingSubmission(
        session: TeacherSession,
        seanceId: String,
        absentIds: Set<String>,
        baseUrl: String,
    ) {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val pending = RoomPendingAttendance(
            id = "${seanceId}-${System.currentTimeMillis()}",
            seanceId = seanceId,
            absentStudentIds = absentIds.toList().sorted().joinToString(","),
            timestamp = timestamp,
            lastStatus = "PENDING",
        )
        db.pendingAttendanceDao().save(pending)
    }

    suspend fun syncPending(session: TeacherSession, baseUrl: String): Boolean {
        val pendingItems = db.pendingAttendanceDao().getAllPending()
        if (pendingItems.isEmpty()) return true

        val api = LaravelApiClient(baseUrl).service
        var allSucceeded = true
        for (item in pendingItems) {
            val response = api.submitAttendance(
                seance = item.seanceId,
                authorization = "Bearer ${session.token}",
                body = AttendanceRequestDto(
                    absent_student_ids = item.absentStudentIds.split(",").filter { it.isNotBlank() },
                    timestamp = item.timestamp,
                ),
            )
            if (response.isSuccessful) {
                db.pendingAttendanceDao().deleteById(item.id)
            } else {
                allSucceeded = false
            }
        }

        return allSucceeded
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
