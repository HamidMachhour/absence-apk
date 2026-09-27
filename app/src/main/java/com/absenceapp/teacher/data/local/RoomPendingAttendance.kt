package com.absenceapp.teacher.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_attendance")
data class RoomPendingAttendance(
    @PrimaryKey val id: String,
    val seanceId: String,
    val absentStudentIds: String,
    val timestamp: String,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
    val lastStatus: String = "PENDING",
)
