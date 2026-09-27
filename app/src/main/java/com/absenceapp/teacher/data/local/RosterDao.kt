package com.absenceapp.teacher.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RosterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoster(roster: RoomSeanceRoster)

    @Query("SELECT * FROM rosters WHERE seanceId = :seanceId LIMIT 1")
    suspend fun getRosterBySeance(seanceId: String): RoomSeanceRoster?

    @Query("SELECT * FROM rosters ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLastRoster(): RoomSeanceRoster?

    @Query("SELECT * FROM rosters ORDER BY updatedAt DESC")
    fun observeAllRosters(): Flow<List<RoomSeanceRoster>>
}
