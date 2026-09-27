package com.absenceapp.teacher.data.remote

import com.absenceapp.teacher.domain.SeanceRoster
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

object StringOrNumberSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrNumber", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: error("This serializer only supports JsonDecoder")
        val primitive = jsonDecoder.decodeSerializableValue(JsonPrimitive.serializer())
        return primitive.content
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}

interface LaravelApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<AuthResponseDto>

    @GET("/api/v1/teachers/me/seances")
    suspend fun getTeacherSeances(
        @Header("Authorization") authorization: String,
    ): Response<TeacherSeancesResponseDto>

    @GET("/api/v1/seances/{seance}/roster")
    suspend fun getRoster(
        @Path("seance") seance: String,
        @Header("Authorization") authorization: String,
    ): Response<RosterResponseDto>

    @POST("/api/v1/seances/{seance}/attendance")
    suspend fun submitAttendance(
        @Path("seance") seance: String,
        @Header("Authorization") authorization: String,
        @Body body: AttendanceRequestDto,
    ): Response<Unit>
}

@kotlinx.serialization.Serializable
data class AuthResponseDto(
    val token: String? = null,
    val user: UserDto? = null,
    val teacher: TeacherDto? = null,
    @Serializable(with = StringOrNumberSerializer::class)
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
)

@kotlinx.serialization.Serializable
data class UserDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
)

@kotlinx.serialization.Serializable
data class TeacherDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
)

@kotlinx.serialization.Serializable
data class TeacherSeancesResponseDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val teacher_id: String? = null,
    val seances: List<TeacherSeanceDto> = emptyList(),
)

@kotlinx.serialization.Serializable
data class TeacherSeanceDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val id: String = "",
    val day_id: Int? = null,
    val day_name: String? = null,
    val salle: String? = null,
    val subject: String? = null,
    val classe_name: String? = null,
    val de: String? = null,
    val a: String? = null,
    val time_range: String? = null,
    val time_range_short: String? = null,
)

@kotlinx.serialization.Serializable
data class RosterResponseDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val seance_id: String? = null,
    val classe_name: String? = null,
    val subject: String? = null,
    val salle: String? = null,
    val students: List<StudentDto> = emptyList(),
)

@kotlinx.serialization.Serializable
data class StudentDto(
    @Serializable(with = StringOrNumberSerializer::class)
    val id: String,
    val num: Int = 0,
    val name: String,
    val photo_url: String? = null,
)

@kotlinx.serialization.Serializable
data class AttendanceRequestDto(
    val absent_student_ids: List<String> = emptyList(),
    val timestamp: String,
)
