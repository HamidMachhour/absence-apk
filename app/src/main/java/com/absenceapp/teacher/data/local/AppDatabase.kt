package com.absenceapp.teacher.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomSeanceRoster::class, RoomPendingAttendance::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rosterDao(): RosterDao
    abstract fun pendingAttendanceDao(): PendingAttendanceDao
}
