# Absence APK

Sample Android attendance app for teacher workflows with Laravel + Mosquitto integration.

## Overview

This project is a minimal but functional teacher attendance app built with Kotlin, Jetpack Compose, Retrofit, Room, DataStore, WorkManager and MQTT. It supports teacher login, seance selection, roster loading, attendance marking, attendance submission, saved last roster access, and real-time MQTT seance notifications.

## Included features

- Teacher login against Laravel with token persistence
- Session persistence using DataStore
- Teacher seance selection and roster loading
- Local caching of rosters in Room
- Open last saved roster quickly from local cache
- Attendance toggling for each student with present/absent states
- Attendance submission to Laravel
- Pending sync / retry logic for queued attendance submissions
- MQTT connection and subscription to teacher seance alerts
- Collapsible session header and roster summary cards
- Navigation drawer with logout action
- English and Arabic language switch
- Persistent language choice
- Mobile-friendly Compose UI optimized for teacher usage

## Screens and flows

### Login screen

- Laravel base URL field
- email and password fields
- MQTT host, port, username and password settings
- language toggle (EN / AR)
- login button with loading state
- error handling for failed login

### Home screen

- teacher welcome header
- drawer menu accessible from top-left icon
- language switch from drawer or top bar
- collapsible session card containing:
  - MQTT status
  - last MQTT message
  - seance selector
- buttons to:
  - load roster
  - open last roster
  - logout

### Roster attendance screen

- collapsed/expanded summary card for the selected seance
- total absent count
- reset attendance button
- submit attendance button
- student list with present/absent switch per row
- scrollable roster list

## Architecture

- Compose UI + ViewModel state
- Retrofit for Laravel API communication
- Room database for roster cache and local pending sync data
- DataStore for auth, settings, and language preference
- MQTT client for push seance notifications
- Local notification channel support for MQTT alerts

## Project structure

- `app/src/main/java/com/absenceapp/teacher/data` – session storage, repositories, DTOs
- `app/src/main/java/com/absenceapp/teacher/domain` – domain models and enums
- `app/src/main/java/com/absenceapp/teacher/mqtt` – MQTT manager and message parsing
- `app/src/main/java/com/absenceapp/teacher/network` – Retrofit base client and API configuration
- `app/src/main/java/com/absenceapp/teacher/ui` – Compose screens, drawer UI, and ViewModel
- `app/src/main/java/com/absenceapp/teacher/data/local` – Room entities and DAO layer

## Backend contract

The app expects the following Laravel endpoints:

- `POST /api/auth/login`
- `GET /api/v1/teachers/me/seances`
- `GET /api/v1/seances/{seance}/roster`
- `POST /api/v1/seances/{seance}/attendance`

MQTT topic:

- `teacher/{teacherId}/seances`

## Local setup

1. Install Android Studio and the Android SDK.
2. Open this folder in Android Studio.
3. Set the SDK path if prompted.
4. On a real phone, use your machine's LAN IP instead of `127.0.0.1`.
5. In the login form, set the Laravel base URL and MQTT details.
6. Run the app on a real device or emulator.

Example values:

- Laravel base URL: `http://192.168.1.2:8000`
- MQTT host: `192.168.1.2`
- MQTT port: `1883`

## Language support

The app supports:

- English (`EN`)
- Arabic (`AR`)

The selected language is saved and restored automatically between sessions.

## Build verification

This project was verified with:

```bash
cd /mnt/data_nvme/absence_app_project/absence-apk
export ANDROID_HOME=/usr/lib/android-sdk
export ANDROID_SDK_ROOT=/usr/lib/android-sdk
/home/hamid/.local/gradle-8.7/bin/gradle assembleDebug
```

Result: `BUILD SUCCESSFUL`.

## Notes

- This sample app is intended for local development and demo usage.
- It is intentionally lightweight and easy to adapt to your Laravel backend.
- The roster response should return valid JSON for the app to parse correctly.
- MQTT and Laravel settings should use the same LAN network values when running on a physical phone.

