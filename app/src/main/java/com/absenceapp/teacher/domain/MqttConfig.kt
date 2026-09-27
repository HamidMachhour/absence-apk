package com.absenceapp.teacher.domain

data class MqttConfig(
    val host: String = "",
    val port: Int = 1883,
    val username: String = "",
    val password: String = "",
)
