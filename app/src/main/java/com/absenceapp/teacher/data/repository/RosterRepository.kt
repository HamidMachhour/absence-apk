package com.absenceapp.teacher.data.repository

import com.absenceapp.teacher.data.local.AppDatabase
import com.absenceapp.teacher.data.local.RoomSeanceRoster
import com.absenceapp.teacher.data.local.StoredStudent
import com.absenceapp.teacher.data.remote.LaravelApiService
import com.absenceapp.teacher.data.remote.RosterResponseDto
import com.absenceapp.teacher.domain.SeanceRoster
import com.absenceapp.teacher.domain.StudentRosterItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class RosterRepository(
    private val api: LaravelApiService,
    private val db: AppDatabase,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchRoster(seanceId: String, token: String): SeanceRoster {
        val response = api.getRoster(seanceId, "Bearer $token")
        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val dto = response.body() ?: throw IllegalStateException("Missing roster payload")
        val roster = dto.toDomain()
        val stored = roster.students.map {
            StoredStudent(
                id = it.id,
                num = it.num,
                name = it.name,
                photoUrl = it.photoUrl,
                isAbsent = it.isAbsent,
            )
        }
        db.rosterDao().saveRoster(
            RoomSeanceRoster(
                seanceId = roster.seanceId,
                classeName = roster.classeName,
                subject = roster.subject,
                salle = roster.salle,
                studentsJson = json.encodeToString(stored),
            ),
        )
        return roster
    }

    suspend fun getLastSavedRoster(): SeanceRoster? {
        val local = db.rosterDao().getLastRoster() ?: return null
        return local.toDomain(json)
    }

    suspend fun getRosterBySeance(seanceId: String): SeanceRoster? {
        val local = db.rosterDao().getRosterBySeance(seanceId) ?: return null
        return local.toDomain(json)
    }
}

private fun RoomSeanceRoster.toDomain(json: Json): SeanceRoster {
    val students = runCatching {
        json.decodeFromString<List<StoredStudent>>(studentsJson)
    }.getOrDefault(emptyList())

    return SeanceRoster(
        seanceId = seanceId,
        classeName = classeName,
        subject = subject,
        salle = salle,
        students = students.map {
            StudentRosterItem(
                id = it.id,
                num = it.num,
                name = it.name,
                photoUrl = it.photoUrl,
                isAbsent = it.isAbsent,
            )
        },
    )
}

private fun RosterResponseDto.toDomain(): SeanceRoster {
    return SeanceRoster(
        seanceId = seance_id ?: "",
        classeName = classe_name ?: "",
        subject = subject ?: "",
        salle = salle ?: "",
        students = students.map {
            StudentRosterItem(
                id = it.id,
                num = it.num,
                name = it.name,
                photoUrl = it.photo_url,
                isAbsent = false,
            )
        },
    )
}
