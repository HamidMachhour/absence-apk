package com.absenceapp.teacher.network

import com.absenceapp.teacher.data.remote.LaravelApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class LaravelApiClient(baseUrl: String) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        isLenient = true
    }

    private var baseUrlValue: String = baseUrl.trimEnd('/') + "/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrlValue)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val service: LaravelApiService = retrofit.create(LaravelApiService::class.java)

    fun updateBaseUrl(newBaseUrl: String) {
        baseUrlValue = newBaseUrl.trimEnd('/') + "/"
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrlValue)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
