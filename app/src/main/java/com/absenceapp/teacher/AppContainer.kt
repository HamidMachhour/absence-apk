package com.absenceapp.teacher

import android.content.Context
import androidx.room.Room
import com.absenceapp.teacher.data.SessionStore
import com.absenceapp.teacher.data.local.AppDatabase
import com.absenceapp.teacher.data.repository.AttendanceRepository
import com.absenceapp.teacher.data.repository.AuthRepository
import com.absenceapp.teacher.data.repository.RosterRepository
import com.absenceapp.teacher.mqtt.MqttManager
import com.absenceapp.teacher.network.LaravelApiClient

class AppContainer(private val context: Context) {
    val sessionStore = SessionStore(context)

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "absence_app_db",
    ).fallbackToDestructiveMigration().build()

    val laravelApiClient = LaravelApiClient("http://10.0.0.11:8000")
    val authRepository = AuthRepository(laravelApiClient.service, context)
    val rosterRepository = RosterRepository(laravelApiClient.service, db)
    val attendanceRepository = AttendanceRepository(context, db)
    val mqttManager = MqttManager(context)

    fun updateApiBaseUrl(baseUrl: String) {
        laravelApiClient.updateBaseUrl(baseUrl)
    }
}
