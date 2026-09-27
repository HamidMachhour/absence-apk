package com.absenceapp.teacher.mqtt

import android.content.Context
import android.util.Log
import com.absenceapp.teacher.domain.MqttConfig
import com.absenceapp.teacher.domain.MqttEvent
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

class MqttManager(private val context: Context) {
    private var client: MqttAndroidClient? = null
    private var connected = false
    private var lastMessage: MqttEvent? = null
    private val listeners = mutableListOf<(MqttEvent) -> Unit>()

    fun addListener(listener: (MqttEvent) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (MqttEvent) -> Unit) {
        listeners.remove(listener)
    }

    fun isConnected(): Boolean = connected
    fun lastMessage(): MqttEvent? = lastMessage

    suspend fun connect(config: MqttConfig, teacherId: String) = suspendCancellableCoroutine { cont ->
        val url = "tcp://${config.host}:${config.port}"
        client = MqttAndroidClient(context, url, "teacher_${teacherId}_${System.currentTimeMillis()}")
        client?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                connected = false
                Log.e("MqttManager", "Connection lost", cause)
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val payload = message?.payload?.let { String(it, StandardCharsets.UTF_8) } ?: ""
                val event = MqttEvent(
                    event = "",
                    seanceId = "",
                    salle = "",
                    message = payload,
                    topic = topic ?: "",
                    rawPayload = payload,
                )
                lastMessage = event
                val parsed = parsePayload(payload, topic ?: "")
                if (parsed != null) {
                    lastMessage = parsed
                    listeners.forEach { it(parsed) }
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) = Unit
        })

        val options = MqttConnectOptions().apply {
            isCleanSession = true
            if (config.username.isNotBlank()) {
                userName = config.username
            }
            if (config.password.isNotBlank()) {
                password = config.password.toCharArray()
            }
        }

        client?.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                connected = true
                cont.resume(Unit)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                connected = false
                cont.resumeWithException(exception ?: IllegalStateException("Mqtt connect failed"))
            }
        })
    }

    suspend fun subscribeToTeacherAlerts(teacherId: String) = suspendCancellableCoroutine { cont ->
        val topic = "teacher/$teacherId/seances"
        client?.subscribe(topic, 1, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                cont.resume(Unit)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                cont.resumeWithException(exception ?: IllegalStateException("Subscribe failed"))
            }
        })
    }

    fun disconnect() {
        client?.disconnect()
        connected = false
    }

    private fun parsePayload(payload: String, topic: String): MqttEvent? {
        val clean = payload.trim()
        if (clean.isBlank()) return null
        return try {
            val eventText = Regex("\"event\"\\s*:\\s*\"([^\"]+)\"").find(clean)?.groupValues?.get(1) ?: ""
            val seanceId = Regex("\"seance_id\"\\s*:\\s*\"([^\"]+)\"").find(clean)?.groupValues?.get(1) ?: ""
            val salle = Regex("\"salle\"\\s*:\\s*\"([^\"]+)\"").find(clean)?.groupValues?.get(1) ?: ""
            val message = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(clean)?.groupValues?.get(1) ?: clean
            MqttEvent(
                event = eventText,
                seanceId = seanceId,
                salle = salle,
                message = message,
                topic = topic,
                rawPayload = clean,
            )
        } catch (_: Throwable) {
            null
        }
    }
}
