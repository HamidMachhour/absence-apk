package com.absenceapp.teacher.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PendingAttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: RoomPendingAttendance)

    @Update
    suspend fun update(item: RoomPendingAttendance)

    @Query("SELECT * FROM pending_attendance ORDER BY createdAt DESC")
    suspend fun getAllPending(): List<RoomPendingAttendance>

    @Query("SELECT * FROM pending_attendance WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): RoomPendingAttendance?

    @Query("DELETE FROM pending_attendance WHERE id = :id")
    suspend fun deleteById(id: String)
}
