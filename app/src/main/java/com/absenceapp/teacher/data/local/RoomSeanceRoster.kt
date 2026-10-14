package com.absenceapp.teacher.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Entity(tableName = "rosters")
@TypeConverters(StudentListConverter::class)
data class RoomSeanceRoster(
    @PrimaryKey val seanceId: String,
    val classeName: String,
    val subject: String,
    val salle: String,
    val studentsJson: String,
    val updatedAt: Long = System.currentTimeMillis(),
)

class StudentListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromString(value: String): List<StoredStudent> =
        runCatching {
            json.decodeFromString(ListSerializer(StoredStudent.serializer()), value)
        }.getOrDefault(emptyList())

    @TypeConverter
    fun toString(students: List<StoredStudent>): String =
        json.encodeToString(ListSerializer(StoredStudent.serializer()), students)
}

@Serializable
data class StoredStudent(
    val id: String,
    val userId: String,
    val num: Int = 0,
    val name: String,
    val photoUrl: String? = null,
    val isAbsent: Boolean = false,
)
